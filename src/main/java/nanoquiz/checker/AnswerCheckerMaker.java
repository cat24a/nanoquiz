package nanoquiz.checker;

import java.util.AbstractMap;
import java.util.Map;

import nanoquiz.Main;

@FunctionalInterface
public interface AnswerCheckerMaker {
	AnswerChecker make(String correctAnswer);

	public static final Map<String, AnswerCheckerMaker> makers = Map.<String, AnswerCheckerMaker>ofEntries(
		new AbstractMap.SimpleImmutableEntry<>("ci", CaseInsensitiveAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("case_insensitive", CaseInsensitiveAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("literal", LiteralAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("cs", CaseSensitiveAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("case_sensitive", CaseSensitiveAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("multi", MultipleAnswerCheckers::new),
		new AbstractMap.SimpleImmutableEntry<>("multiple", MultipleAnswerCheckers::new),
		new AbstractMap.SimpleImmutableEntry<>("renamed", RenamedAnswerChecker::new),
		new AbstractMap.SimpleImmutableEntry<>("umlaut", UmlautAnswerChecker::new)
	);

	public static AnswerChecker parseAnswer(String input) {
		return parseAnswer(input, CaseInsensitiveAnswerChecker::new);
	}

	public static AnswerChecker parseAnswer(String input, AnswerCheckerMaker defaultChecker) {
		if(input.charAt(0) == ' ') input = input.substring(1);

		if(input.charAt(0) != '(')
			return defaultChecker.make(input);

		int end = input.indexOf(')');
		if(end == -1) {
			Main.log.warning(()->"Question has an opening bracket but no closing one.");
			return defaultChecker.make(input);
		}
		
		String checkerName = input.substring(1, end).toLowerCase();
		AnswerCheckerMaker checkerType = makers.get(checkerName);
		if(checkerType == null) {
			Main.log.warning(()->"Unknown answer checker: \"" + checkerName + "\".");
			return defaultChecker.make(input);
		}
		
		return checkerType.make(input.substring(end+1));
	}
}
