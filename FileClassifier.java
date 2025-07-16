

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileClassifier {

    private static final String DOWNLOADS_DIR = System.getProperty("user.home") + "\\Downloads";
    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + "\\organizer.log";

    public static void organizeFiles() {
        File downloadFolder = new File(DOWNLOADS_DIR);

        if (!downloadFolder.exists()) {
            System.out.println("Downloads folder not found!");
            return;
        }

        File[] files = downloadFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                moveFileToCategory(file);
            }
        }

        System.out.println("Files organized successfully.");
    }

    private static void moveFileToCategory(File file) {
        String name = file.getName().toLowerCase();

        String categoryFolder = switch (getExtension(name)) {
            case "jpg", "png", "jpeg", "gif" -> "Images";
            case "pdf", "docx", "txt", "pptx" -> "Documents";
            case "mp4", "mkv", "mov"          -> "Videos";
            case "zip", "rar", "7z"           -> "Archives";
            case "exe", "msi"                 -> "Software";
            default -> "Others";
        };

        File destDir = new File(file.getParent(), categoryFolder);
        destDir.mkdirs();

        try {
            Path source = file.toPath();
            Path destination = new File(destDir, file.getName()).toPath();
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);

            // Log success
            logAction("Moved \"" + source + "\" → \"" + destination + "\"");

        } catch (Exception e) {
            logAction(" Failed to move: " + file.getName() + " → " + e.getMessage());
        }
    }

    private static String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return (dot == -1) ? "" : filename.substring(dot + 1);
    }

    private static void logAction(String message) {
        try {
            File logFolder = new File(LOG_DIR);
            if (!logFolder.exists()) logFolder.mkdirs();

            FileWriter writer = new FileWriter(LOG_FILE, true);
            String timeStamp = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss] ").format(new Date());
            writer.write(timeStamp + message + System.lineSeparator());
            writer.close();
        } catch (IOException e) {
            System.err.println("⚠️ Logging failed: " + e.getMessage());
        }
    }
}
