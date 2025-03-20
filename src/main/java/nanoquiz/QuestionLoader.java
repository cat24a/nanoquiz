package nanoquiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nanoquiz.checker.AnswerCheckerMaker;

public abstract class QuestionLoader {
    static void loadQuestions() {
        File quizDir = Main.workdir.resolve("quiz").toFile();
        quizDir.mkdir();
        for (File quizFile : quizDir.listFiles()) {
            try {
                parseSimpleQuiz(quizFile);
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static void parseSimpleQuiz(File file) throws FileNotFoundException {
        try(BufferedReader reader = new BufferedReader(new FileReader(file));) {
            while (true) {
                String question = reader.readLine();
                String answer = reader.readLine();
                if (question == null || answer == null) {
                    break;
                }
                Main.questions.add(new Question(question, AnswerCheckerMaker.parseAnswer(answer)));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
