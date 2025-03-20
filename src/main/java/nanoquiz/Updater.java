package nanoquiz;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.json.JSONObject;

public abstract class Updater {
    static final String updatesApiUriAsString = "https://api.github.com/repos/cat24a/nanoquiz/releases/latest";
    public static final String VERSION = "24w43c";
    static final String updateDownloadUrl = "https://github.com/cat24a/nanoquiz";

    public static void checkForUpdates() {
        if (Config.CHECK_FOR_UPDATES) {
            Main.log.info(() -> "Checking for updates…");
            new Thread(Updater::run).start();
        }
    }

    static void run() {
        URI updatesApiUri;
        try {
            updatesApiUri = new URI(updatesApiUriAsString);
        } catch (URISyntaxException e) {
            Main.log.severe(() -> "There is something wrong with the API address.");
            e.printStackTrace();
            return;
        }

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder(updatesApiUri)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject data = new JSONObject(response.body());
            String latest = data.getString("name");
            if (VERSION.equals(latest)) {
                Main.log.info(() -> "This is the latest version.");
                return;
            }
            Main.log.info(() -> "There is a new version available: " + latest);
            showUpdateMessage();
        } catch (IOException e) {
            Main.log.warning(() -> "IOError while requesting updates. Check your internet connection.");
            e.printStackTrace();
        } catch (InterruptedException e) {
            Main.log.severe(() -> "Updater interrupted.");
            e.printStackTrace();
            return;
        }
    }

    static void showUpdateMessage() {
        SwingUtilities.invokeLater(()->{
            JFrame window = new JFrame("Aktualizacja NanoQuiz");
            window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            window.setLocationRelativeTo(null);
            JLabel label = new JLabel("<html><p>Dostępna jest nowa wersja NanoQuiz. Pobierz ją tu: " + updateDownloadUrl + "</p><p>Możesz ukryć tę wiadomość w ustawieniach programu.</p></html>");
            label.setPreferredSize(new Dimension(350, 100));
            window.add(label);
            window.pack();
            window.setVisible(true);
        });
    }
}
