package nanoquiz.checker;

import nanoquiz.Main;

public class RenamedAnswerChecker implements AnswerChecker {
	private AnswerChecker checker;
	private String answerName;

	public RenamedAnswerChecker(String input) {
		String[] parts = input.split(";", 2);
		answerName = parts[0].strip();
		if(parts.length == 1) { // incorrect format
			Main.log.warning(()->"Incorrect format for \"renamed\" answer checker");
			checker = new CaseInsensitiveAnswerChecker("");
		} else {
			if(parts[1].startsWith(" "))
				parts[1] = parts[1].substring(1);
			checker = AnswerCheckerMaker.parseAnswer(parts[1]);
		}
	}

	@Override
	public boolean check(String userAnswer) {
		return checker.check(userAnswer);
	}

	@Override
	public String getCorrectAnswer() {
		return answerName;
	}
	
}
