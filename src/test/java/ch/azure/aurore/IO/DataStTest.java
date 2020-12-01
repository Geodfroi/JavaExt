package ch.azure.aurore.IO;

import ch.azure.aurore.IO.API.FileHelper;
import org.junit.jupiter.api.Assertions;

import java.util.Optional;

class DataStTest {

    public static final String FILE_PATH = "C:\\Network\\TestFolder\\TestFile.txt";
    private static final String FOLDER_PATH = "C:\\Network\\TestFolder";
    //public static final String FILENAME = "TestFile.txt";

    @org.junit.jupiter.api.Test
    void backupFile()
    {
        FileHelper.backupFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void getExtension()
    {
        Optional<String> result = FileHelper.getExtension(FILE_PATH);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get(), ".txt");
    }

    @org.junit.jupiter.api.Test
    void getExtension_severalPoints()
    {
        Optional<String> result = FileHelper.getExtension("fileWithSeveral.point.txt");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get(), ".txt");
    }

    @org.junit.jupiter.api.Test
    void getExtension_invalid()
    {
        Optional<String> result = FileHelper.getExtension("invalidStrWithoutPoint");
        Assertions.assertTrue(result.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getFileNameWithoutExt()
    {
        String result = FileHelper.getFileNameWithoutExt(FILE_PATH);
        Assertions.assertEquals(result, "TestFile");
    }

    @org.junit.jupiter.api.Test
    void openFile_file(){
        FileHelper.openFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void openFile_invalid(){
        FileHelper.openFile("invalidStr");
    }
    @org.junit.jupiter.api.Test
    void openFile_null(){
        FileHelper.openFile(null);
    }

    @org.junit.jupiter.api.Test
    void openFile_folder(){
        FileHelper.openFile(FOLDER_PATH);
    }
}