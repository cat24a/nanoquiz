package nanoquiz.checker;

public class CaseInsensitiveAnswerChecker implements AnswerChecker {
    String correctAnswer;

    public CaseInsensitiveAnswerChecker(String correctAnswer) {
        this.correctAnswer = correctAnswer.strip();
    }

    @Override
    public boolean check(String userAnswer) {
        return correctAnswer.toLowerCase().equals(userAnswer.strip().toLowerCase());
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
