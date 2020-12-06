package ch.azure.aurore.IO.API;

import ch.azure.aurore.Strings.Strings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.awt.Desktop;

public class Disk {

    public static final String BACKUP_FOLDER_NAME = "Backups";

    /**
     *
     * @param filename a file path as string;
     * @return returns the extension including the \. character IE ".txt"; returns an empty optional if fileName parameter doesn't contain a \. character;
     */
    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")));
    }

    public static String getFileNameWithoutExt(String pathStr) {
        pathStr = Path.of(pathStr).getFileName().toString();
        return pathStr.substring(0, pathStr.lastIndexOf("."));
    }

    public static void backupFile(String pathStr) {
        Path path1 = Path.of(pathStr);
        if (!Files.exists(path1))
            return;

        Path parentPath = path1.getParent();
        Path backupPath = Path.of(parentPath.toString(), BACKUP_FOLDER_NAME);

        if (!Files.exists(backupPath)) {
            try {
                Files.createDirectory(backupPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String fileName = path1.getFileName().toString();
        String rootName = Disk.getFileNameWithoutExt(fileName);

        Optional<String> extValue = Disk.getExtension(fileName);
        if (extValue.isEmpty()) {
            System.out.println("backup failed : Can't get file extension");
            return;
        }

        String copyName = rootName + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyLLdd")) + extValue.get();
        Path destination = backupPath.resolve(copyName);
        System.out.println(destination);

        try {
            Files.copy(path1, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println(path1 + " backed up to " + destination);
        } catch (IOException e) {
            System.out.println("Backup failed: " + e.getMessage());
        }
    }

    public static void openFile(File file){
        if (file == null){
            return;
        }
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            System.out.println("openFile failed: " + e.getMessage());
        }
    }

    public static void openFile(String pathStr){
        if (pathStr == null || pathStr.isEmpty() || pathStr.isBlank() )
            return;
        openFile(new File(pathStr));
    }

    /**
     * Delete file from disk
     * @param pathStr the path on disk
     * @return true if file was correctly deleted
     */
    public static boolean removeFile(String pathStr){
        try {
            return Files.deleteIfExists(Paths.get(pathStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String removeExtension(String str){
        if (Strings.isNullOrEmpty(str))
            return "";
        if (!str.contains("."))
            return str;

        return str.substring(0, str.lastIndexOf("."));
    }
}
