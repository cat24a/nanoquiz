package nanoquiz.checker;

public class MultipleAnswerCheckers implements AnswerChecker {
	private AnswerChecker[] checkers;

	public MultipleAnswerCheckers(String data) {
		String[] elements = data.split(";");
		checkers = new AnswerChecker[elements.length];
		for(int i = 0; i < elements.length; i++) {
			if(elements[i].startsWith(" "))
				elements[i] = elements[i].substring(1);
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
