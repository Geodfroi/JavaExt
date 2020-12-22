package ch.azure.aurore.IO.API;

import ch.azure.aurore.strings.Strings;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Disk {

    public static final String BACKUP_FOLDER_NAME = "Backups";
    private static final int BACKUP_DEFAULT_FILE_LIMIT = 6;

    /**
     * @param filename a file path as string;
     * @return returns the extension including the \. character IE ".txt"; returns an empty optional if fileName parameter doesn't contain a \. character;
     */
    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".")));
    }

    public static String getFileName(String pathStr, Boolean withExtension) {
        if (Strings.isNullOrEmpty(pathStr))
            return "";

        String fileName = Path.of(pathStr).getFileName().toString();
        if (withExtension)
            return fileName;
        else
            return removeExtension(fileName);
    }

    public static boolean backupFile(String pathStr) {
        return backupFile(pathStr, BACKUP_DEFAULT_FILE_LIMIT);
    }

    public static boolean backupFile(String pathStr, int fileLimit) {
        Path path1 = Path.of(pathStr);
        if (!Files.exists(path1) || !Files.isRegularFile(path1))
            return false;

        Optional<String> extValue = Disk.getExtension(pathStr);
        if (extValue.isEmpty()) {
            System.out.println("backup failed : Can't get file extension");
            return false;
        }

        Path parentPath = path1.getParent();
        Path backupPath = Path.of(parentPath.toString(), BACKUP_FOLDER_NAME);

        String fileName = path1.getFileName().toString();
        String fileNameWithoutExt = Disk.removeExtension(fileName);

        try {
            if (!Files.exists(backupPath)) {
                Files.createDirectories(backupPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String copyName = fileNameWithoutExt + "_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyLLdd")) + extValue.get();
        Path destination = backupPath.resolve(copyName);

        try {
            Files.copy(path1, destination, StandardCopyOption.REPLACE_EXISTING);

            // identify with regex then sort from newest to oldest
            Pattern pattern = Pattern.compile("^" + fileNameWithoutExt + "_(\\d{8})" + extValue.get() + "$");

            List<Path> backedFiles = Files.list(backupPath).
                    map(f -> new ImmutablePair<>(f, pattern.matcher(f.getFileName().toString()))).
                    filter(p -> p.getValue().matches()).
                    sorted((o1, o2) -> -Integer.compare(Integer.parseInt(o1.getValue().group(1)), Integer.parseInt(o2.getValue().group(1)))).
                    map(ImmutablePair::getKey).
                    collect(Collectors.toList());

            for (int n = 0; n < backedFiles.size(); n++) {
                if (n >= fileLimit) {
                    Files.delete(backedFiles.get(n));
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println("Backup copy failed " + e.getMessage());
            return false;
        }
    }

    public static boolean openFile(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            Desktop.getDesktop().open(file);
            return true;
        } catch (IOException e) {
            System.out.println("openFile failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean openFile(String pathStr) {
        if (pathStr == null || pathStr.isEmpty() || pathStr.isBlank())
            return false;
        return openFile(new File(pathStr));
    }

    public static String removeExtension(String pathStr) {
        if (Strings.isNullOrEmpty(pathStr))
            return "";
        if (!pathStr.contains("."))
            return pathStr;

        return pathStr.substring(0, pathStr.lastIndexOf("."));
    }

    /**
     * Delete file from disk
     *
     * @param pathStr the path on disk
     * @return true if file was correctly deleted
     */
    public static boolean removeFile(String pathStr) {
        try {
            return Files.deleteIfExists(Paths.get(pathStr));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean writeFile(Path path, String content) {

        if (path == null || !Files.exists(path) || !Files.isRegularFile(path))
            return false;

        try {
            Files.writeString(path, content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("failed to write file: " + path);
            return false;
        }
    }
}