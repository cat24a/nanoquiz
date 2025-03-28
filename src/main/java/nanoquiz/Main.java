package nanoquiz;

import java.awt.Color;
import java.awt.HeadlessException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import nanoquiz.ui.CLI;
import nanoquiz.ui.DesktopUI;
import nanoquiz.ui.UI;
import nanoquiz.util.Timer;

public abstract class Main {
    public static final Logger log = Logger.getLogger("nanoquiz");
    static UI ui;
    static List<Question> questions = new ArrayList<>();
    public static Path workdir = Path.of(".");

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException {
        try {
            ui = new DesktopUI(Thread.currentThread()::interrupt);
        } catch(InvocationTargetException e) {
            if(e.getCause() instanceof HeadlessException)
            ui = new CLI();
            else
            throw e;
        }
        
        ui.setText("Uruchamianie NanoQuiz...", Color.GRAY, false, true);
        
        setupFS();
        Config.loadConfig();
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
            ui.setText(question.question, Color.BLACK, true, true);

            String answer = ui.getAnswer();
            boolean good = question.answer(answer);
            
            if (good) {
                ui.setText("DOBRZE!", Color.GREEN, false, false);
                Thread.sleep(Config.GOOD_DELAY);
            } else {
                ui.setText("ŹLE: " + question.answerChecker.getCorrectAnswer(), Color.RED, false, false);
                Thread.sleep(Config.BAD_DELAY);
            }
            if(Config.COOLDOWN != 0) {
                ui.hide();
            }
            Timer timer = new Timer(Config.COOLDOWN);
            question = chooseQuestion();
            timer.join();
            
        }
    }

    static void setupFS() {
        if(workdir.resolve("nanoquiz-workdir-here").toFile().exists()) {
            log.info(()->"Found workdir override - skipping workdir config.");
            return;
        }

        try {
            workdir = Path.of(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
        } catch(URISyntaxException e) {}
    }

    static Question chooseQuestion() {
        Collections.shuffle(questions);
        Question best = null;
        float bestDifficulty = Float.NEGATIVE_INFINITY;
        int checkedAmount = (int) (questions.size() * Config.CONFIDENCE);
        if(checkedAmount > questions.size()){
            checkedAmount = questions.size();
        }
        if(checkedAmount <= 0) {
            checkedAmount = 1;
        }
        for(int i = 0; i < checkedAmount; i++) {
            Question question = questions.get(i);
            float difficulty = (float) (System.currentTimeMillis() - question.lastQuizzed);
            difficulty -= question.score * Config.SCORE_FACTOR;
            if(difficulty > bestDifficulty) {
                best = question;
                bestDifficulty = difficulty;
            }
        }
        return best;
    };
}
