package nanoquiz.ui;

import java.awt.Color;

public interface UI {
	void setText(String text, Color textColor, boolean inputEnabled, boolean resetInput);
	void hide();
	String getAnswer() throws InterruptedException;

	@FunctionalInterface
    public static interface QuitHandler {
        void handleQuit();
    }
}
