package nanoquiz.ui;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI implements UI {

	BufferedReader answerReader;

	public CLI() {
		answerReader = new BufferedReader(new InputStreamReader(System.in));
	}

	@Override
	public void setText(String text, Color textColor, boolean inputEnabled, boolean resetInput) {
		System.out.println(text);
	}

	@Override
	public void hide() {
		// do nothing
	}

	@Override
	public String getAnswer() {
		try {
			return answerReader.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
