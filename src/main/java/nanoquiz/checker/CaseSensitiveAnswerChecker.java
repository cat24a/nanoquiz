package nanoquiz.checker;

public class CaseSensitiveAnswerChecker implements AnswerChecker {
	String correctAnswer;

    public CaseSensitiveAnswerChecker(String correctAnswer) {
        this.correctAnswer = correctAnswer.strip();
    }

    @Override
    public boolean check(String userAnswer) {
        return correctAnswer.equals(userAnswer.strip());
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
