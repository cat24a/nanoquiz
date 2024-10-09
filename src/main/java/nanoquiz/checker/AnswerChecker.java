package nanoquiz.checker;

public interface AnswerChecker {
    public boolean check(String userAnswer);

    public String getCorrectAnswer();
}
