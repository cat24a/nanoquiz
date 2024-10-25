package nanoquiz;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import com.electronwill.nightconfig.core.file.FileConfig;

import nanoquiz.util.Timer;

public abstract class Main {
    public static final Logger log = Logger.getLogger("nanoquiz");
    static UI ui;
    static List<Question> questions = new ArrayList<>();
    static String answer;
    private static final Object answerNotifier = new Object();
    public static final Random random = new Random();
    public static Path workdir = Path.of(".");
    public static FileConfig config;

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        ui = new UI(Thread.currentThread());
        ui.setText("Uruchamianie NanoQuiz...", Color.GRAY, false, true);
        setupFS();
        loadConfig();
        Updater.checkForUpdates();
        QuestionLoader.loadQuestions();
        if(questions.size() == 0) {
            log.severe(()->"NO QUESTIONS");
            ui.setText("BRAK PYTAŃ", Color.RED, false, true);
            return;
        }
        Serialization.loadKnowledge();
        try {
            doQuizThing();
        } catch(InterruptedException e) {
            return;
        } finally {
            Serialization.saveKnowledge();
        }
    }
    
    static void doQuizThing() throws InterruptedException {
        Question question = chooseQuestion();
        while(true) {
            boolean good;
            synchronized(answerNotifier) {
                answer = null;
            }
            ui.setText(question.question, Color.BLACK, true, true);
            synchronized(answerNotifier) {
                while (answer == null) {
                    answerNotifier.wait();
                }
                good = question.answer(answer);
            }
            
            if (good) {
                ui.setText("DOBRZE!", Color.GREEN, false, false);
                Thread.sleep(config.<Integer>get("delay.good"));
            } else {
                ui.setText("ŹLE: " + question.answerChecker.getCorrectAnswer(), Color.RED, false, false);
                Thread.sleep(config.<Integer>get("delay.bad"));
            }
            if(config.<Integer>get("delay.cooldown") != 0) {
                ui.hide();
            }
            Timer timer = new Timer(config.<Integer>get("delay.cooldown"));
            question = chooseQuestion();
            timer.join();
            
        }
    }

    static void setupFS() {
        try {
            workdir = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch(URISyntaxException e) {}
    }

    static void loadConfig() {
        config = FileConfig.builder(workdir.resolve("nq-config.toml"))
                           .defaultResource("/nanoquiz/default-config.toml")
                           .build();
        config.load();
    }

    static Question chooseQuestion() {
        Collections.shuffle(questions);
        Question best = null;
        float bestDifficulty = Float.NEGATIVE_INFINITY;
        int checkedAmount = (int) (questions.size()*config.<Double>get("question_selector.confidence").floatValue());
        if(checkedAmount > questions.size()){
            checkedAmount = questions.size();
        }
        if(checkedAmount == 0) {
            checkedAmount = 1;
        }
        for(int i = 0; i < checkedAmount; i++) {
            Question question = questions.get(i);
            float difficulty = (float) (System.currentTimeMillis() - question.lastQuizzed);
            difficulty -= question.score * config.<Double>get("question_selector.score_factor").floatValue();
            if(difficulty > bestDifficulty) {
                best = question;
                bestDifficulty = difficulty;
            }
        }
        return best;
    };

    public static void handleSubmit(String answer) {
        synchronized(answerNotifier) {
            Main.answer = answer;
            answerNotifier.notifyAll();
        }
    }
}
