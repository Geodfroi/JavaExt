package ch.azure.aurore.IO.API;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DiskTest {
    public static final String TEST_DIRECTORY_PATH ="C:\\Export\\JavaTestFolder";
    public static final String TEST_FILE_PATH = "C:\\Export\\JavaTestFolder\\FileToErase.txt";
    public static final String TEST_FILE_PATH_NO_EXT = "C:\\Export\\JavaTestFolder\\FileToErase";

    public static final String TEST_NOFILE_PATH = "C:\\Export\\JavaTestFolder\\NoFileThere.txt";

   @org.junit.jupiter.api.BeforeAll
   static void beforeAll() {
       try {
           Files.createDirectories(Paths.get(TEST_DIRECTORY_PATH));
           Files.writeString(Paths.get(TEST_FILE_PATH), "File text content");
       } catch (IOException e) {
           e.printStackTrace();
       }
   }

    @org.junit.jupiter.api.Test
    void deleteFile_existing(){
        boolean result = Disk.removeFile(TEST_FILE_PATH);
        assert result;
    }

    @org.junit.jupiter.api.Test
    void deleteFile_notExisting(){
        boolean result = Disk.removeFile(TEST_NOFILE_PATH);
        assert !result;
    }

    @org.junit.jupiter.api.Test
    void removeExt(){
        String str = Disk.removeExtension(TEST_FILE_PATH);
        assert str.equals(TEST_FILE_PATH_NO_EXT);
    }

    @org.junit.jupiter.api.Test
    void removeExt_notExtStr(){
        String str = Disk.removeExtension(TEST_FILE_PATH_NO_EXT);
        assert str.equals(TEST_FILE_PATH_NO_EXT);
    }
}
