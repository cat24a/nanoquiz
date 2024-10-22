package nanoquiz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import nanoquiz.checker.CaseInsensitiveAnswerChecker;

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
        BufferedReader reader = new BufferedReader(new FileReader(file));
        try {
            while (true) {
                String question = reader.readLine();
                String answer = reader.readLine();
                if (question == null || answer == null) {
                    break;
                }
                Main.questions.add(new Question(question, new CaseInsensitiveAnswerChecker(answer)));
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}