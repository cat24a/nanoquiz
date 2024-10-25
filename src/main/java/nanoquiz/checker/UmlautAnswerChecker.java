package nanoquiz.checker;

public class UmlautAnswerChecker implements AnswerChecker {
    AnswerChecker checker;

    UmlautAnswerChecker(String input) {
        if(input.startsWith(" ")) input = input.substring(1);
        checker = AnswerCheckerMaker.parseAnswer(replaceUmlaut(input), CaseSensitiveAnswerChecker::new);
    }

    @Override
    public boolean check(String userAnswer) {
        return checker.check(replaceUmlaut(userAnswer));
    }

    @Override
    public String getCorrectAnswer() {
        return checker.getCorrectAnswer();
    }

    static String replaceUmlaut(String input) {
        return input.replaceAll("a:", "ä")
                    .replaceAll("A:", "Ä")
                    .replaceAll("u:", "ü")
                    .replaceAll("U:", "Ü")
                    .replaceAll("o:", "ö")
                    .replaceAll("O:", "Ö");
    }
    
}
