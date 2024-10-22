package nanoquiz.checker;

import java.util.AbstractMap;
import java.util.Map;

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
		new AbstractMap.SimpleImmutableEntry<>("renamed", RenamedAnswerChecker::new)
	);

	public static AnswerChecker parseAnswer(String input) {
		if(input.charAt(0) != '(')
			return new CaseInsensitiveAnswerChecker(input);

		int end = input.indexOf(')');
		if(end == -1)
			return new CaseInsensitiveAnswerChecker(input);
		
		String checkerName = input.substring(1, end).toLowerCase();
		AnswerCheckerMaker checkerType = makers.get(checkerName);
		if(checkerType == null)
			return new CaseInsensitiveAnswerChecker(input);
		
		return checkerType.make(input.substring(end+1));
	}
}
