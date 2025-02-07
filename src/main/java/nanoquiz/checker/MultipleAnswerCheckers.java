package nanoquiz.checker;

public class MultipleAnswerCheckers implements AnswerChecker {
	private AnswerChecker[] checkers;

	public MultipleAnswerCheckers(String data) {
		String[] elements = data.split(";");
		checkers = new AnswerChecker[elements.length];
		for(int i = 0; i < elements.length; i++) {
			checkers[i] = AnswerCheckerMaker.parseAnswer(elements[i]);
		}
	}

	@Override
	public boolean check(String userAnswer) {
		for(AnswerChecker checker: checkers) {
			if (checker.check(userAnswer)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public String getCorrectAnswer() {
		return checkers[0].getCorrectAnswer();
	}
	
}
