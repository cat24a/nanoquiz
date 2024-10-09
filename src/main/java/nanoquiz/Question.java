package nanoquiz;

import nanoquiz.checker.AnswerChecker;

public class Question {
    public String question;
    AnswerChecker answerChecker;
    public long lastQuizzed = 0L;
    public float score = 0F;

    Question(String question, AnswerChecker answerChecker) {
        this.question = question;
        this.answerChecker = answerChecker;
    }

    public boolean answer(String answer) {
        lastQuizzed = System.currentTimeMillis();
        float scoreDecay = Main.config.<Double>get("question_selector.score_decay").floatValue();
        score *= 1-scoreDecay;
        if(answerChecker.check(answer)) {
            score += scoreDecay;
            return true;
        }
        return false;
    }
}
