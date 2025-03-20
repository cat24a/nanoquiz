package nanoquiz.ui;

import java.awt.Color;

public interface UI {
	void setText(String text, Color textColor, boolean inputEnabled, boolean resetInput);
	void hide();

	@FunctionalInterface
    public static interface QuitHandler {
        void handleQuit();
    }
}
