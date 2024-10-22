package nanoquiz.checker;

public class LiteralAnswerChecker implements AnswerChecker {
	String correctAnswer;

	public LiteralAnswerChecker(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	@Override
	public boolean check(String userAnswer) {
		return correctAnswer.equals(userAnswer);
	}

	@Override
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	
}
