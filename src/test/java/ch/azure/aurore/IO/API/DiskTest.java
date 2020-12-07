package ch.azure.aurore.IO.API;

import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

public class DiskTest {

    private static final String FOLDER_PATH = "TestFolder";
    private static final String FILENAME = "TestFile";
    private static final String EXTENSION = ".txt";
    private static final String BACKUP_FOLDER = FOLDER_PATH + "\\" + "Backups";

    private static final String FILENAME_WITH_EXT = FILENAME + EXTENSION;
    private static final String FILE_PATH_WITHOUT_EXT = FOLDER_PATH + "\\" + FILENAME;
    private static final String FILE_PATH = FILE_PATH_WITHOUT_EXT + EXTENSION;
    private static final String INVALID_FILE_PATH = FOLDER_PATH + "\\" + "dontExist.txt";

    private static final String DELETE_FILE_PATH = FOLDER_PATH + "\\" + "toDelete.txt";

    private static final String TEXT_CONTENT = "Lorem ipsum dolor sit amet...";

    @org.junit.jupiter.api.BeforeAll
    static void beforeAll() {
        try {
            Files.createDirectories(Paths.get(FOLDER_PATH));
            Files.createDirectories(Paths.get(BACKUP_FOLDER));

            Files.writeString(Paths.get(FILE_PATH), TEXT_CONTENT);
            Files.writeString(Paths.get(DELETE_FILE_PATH), TEXT_CONTENT);

            for (int n = 15; n < 26; n++) {
                Path p = Path.of(BACKUP_FOLDER + "\\" +FILENAME + "_201912"+ n + EXTENSION);
                Files.writeString(p, TEXT_CONTENT);
            }
            Path irrelevantFile = Path.of(BACKUP_FOLDER + "\\irrelevant.txt");
            Files.writeString(irrelevantFile, TEXT_CONTENT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @org.junit.jupiter.api.Test
    void backupFile_regularFile(){
        assert Disk.backupFile(FILE_PATH, 6);

        // 6 limit + irrelevant.txt + TestFile_invalid.txt
        assert Objects.requireNonNull(new File(BACKUP_FOLDER).listFiles()).length==8;
    }

    @org.junit.jupiter.api.Test
    void backupFile_invalidFile(){
        assert !Disk.backupFile(INVALID_FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void backupFile_directoryPath(){
        assert !Disk.backupFile(FOLDER_PATH);
    }

    @org.junit.jupiter.api.Test
    void deleteFile_existing(){
        assert Disk.removeFile(DELETE_FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void deleteFile_notExisting(){
        assert !Disk.removeFile(INVALID_FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void backupFile()
    {
        Disk.backupFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void getExtension()
    {
        Optional<String> result = Disk.getExtension(FILE_PATH);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), EXTENSION);
    }

    @org.junit.jupiter.api.Test
    void getExtension_severalPoints()
    {
        Optional<String> result = Disk.getExtension("fileWithSeveral.point" + EXTENSION);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), EXTENSION);
    }

    @org.junit.jupiter.api.Test
    void getExtension_invalid()
    {
        Optional<String> result = Disk.getExtension(FILENAME);
        assert result.isEmpty();
    }

    @org.junit.jupiter.api.Test
    void getFileName_WithoutExt()
    {
        String result = Disk.getFileName(FILE_PATH, false);
        Assertions.assertEquals(result, FILENAME);
    }

    @org.junit.jupiter.api.Test
    void getFileName_WithExt()
    {
        String result = Disk.getFileName(FILE_PATH, true);
        Assertions.assertEquals(result, FILENAME_WITH_EXT);
    }

    @org.junit.jupiter.api.Test
    void openFile_file(){
       assert Disk.openFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void openFile_invalid(){
       assert !Disk.openFile(INVALID_FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void openFile_folder(){
       assert Disk.openFile(FOLDER_PATH);
    }

    @org.junit.jupiter.api.Test
    void removeExt(){
        String str = Disk.removeExtension(FILE_PATH);
        assert str.equals(FILE_PATH_WITHOUT_EXT);
    }

    @org.junit.jupiter.api.Test
    void removeExt_notExtStr(){
        String str = Disk.removeExtension(FILE_PATH_WITHOUT_EXT);
        assert str.equals(FILE_PATH_WITHOUT_EXT);
    }

    @org.junit.jupiter.api.Test
    void writeFile(){
        String pathStr = FOLDER_PATH + "\\writeFile.json";
        assert Disk.writeFile(Path.of(pathStr), TEXT_CONTENT);
    }

    @org.junit.jupiter.api.Test
    void writeFile_invalid(){
        String pathStr =  "notExistFolder\\writeFile.json";
        assert !Disk.writeFile(Path.of(pathStr), TEXT_CONTENT);
    }
}