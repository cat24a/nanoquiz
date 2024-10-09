package nanoquiz;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.reflect.InvocationTargetException;

public class UI {
    public static final Dimension SIZE = new Dimension(400, 100);
    public static final int INPUT_HEIGHT = 40;
    JFrame window;
    JPanel questionUI;
    JLabel question;
    JTextField answer;
    JButton confirm;
    
    public UI(Thread backend) throws InterruptedException, InvocationTargetException {
        ActionListener confirmAction = event->{
            answer.setEnabled(false);
            confirm.setEnabled(false);
            Main.log.fine(()->"Submitted answer: " + answer.getText());
            Main.handleSubmit(answer.getText());
        };

        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                if (backend != null) {
                    backend.interrupt();
                }
            }
        };

        SwingUtilities.invokeAndWait(()->{
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException|InstantiationException|IllegalAccessException|UnsupportedLookAndFeelException e) {
                Main.log.warning(()->"Couldn't set look and feel.");
            }

            window = new JFrame("NanoQuiz Java Edition");
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // window.setResizable(false);
            window.setLocationRelativeTo(null);
            window.addWindowListener(windowListener);

            questionUI = new JPanel(new GridBagLayout());
            questionUI.setPreferredSize(SIZE);

            question = new JLabel("question", JLabel.CENTER);
            questionUI.add(question, new GridBagConstraints(0,0, 2,1, 0,1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));

            answer = new JTextField();
            answer.addActionListener(confirmAction);
            questionUI.add(answer, new GridBagConstraints(0,1, 1,1, 1,0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));

            confirm = new JButton("OK");
            confirm.addActionListener(confirmAction);
            questionUI.add(confirm, new GridBagConstraints(1,1, 1,1, 0,0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,0,0,0), 0,0));

            window.add(questionUI);
            window.pack();
        });
    }

    public void setText(String text, Color textColor, boolean inputEnabled, boolean resetInput) {
        SwingUtilities.invokeLater(()->{
            question.setText("<html><p>" + text + "</html></p>");
            question.setForeground(textColor);
            if(resetInput) {
                answer.setText("");
            }
            answer.setEnabled(inputEnabled);
            confirm.setEnabled(inputEnabled);
            window.setVisible(true);
            if(inputEnabled) {
                answer.requestFocus();
            }
        });
    }

    public void hide() {
        SwingUtilities.invokeLater(()->{
            window.setVisible(false);
        });
    }
}
