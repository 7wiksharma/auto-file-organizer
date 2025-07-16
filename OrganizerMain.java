import java.util.Timer;
import java.util.TimerTask;

public class OrganizerMain {
    public static void main(String[] args) {
        System.out.println("Auto File Organizer Running...");
        FileClassifier.organizeFiles();

        // Optional: Run every 5 minutes
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                FileClassifier.organizeFiles();
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000);
    }
}
