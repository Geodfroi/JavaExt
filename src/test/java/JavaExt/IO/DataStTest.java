package JavaExt.IO;

import org.junit.jupiter.api.Assertions;

import java.util.Optional;

class DataStTest {

    public static final String FILE_PATH = "D:\\Network\\TestFolder\\TestFile.txt";
    private static final String FOLDER_PATH = "D:\\Network\\TestFolder";
    //public static final String FILENAME = "TestFile.txt";

    @org.junit.jupiter.api.Test
    void backupFile()
    {
        DataSt.backupFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void getExtension()
    {
        Optional<String> result = DataSt.getExtension(FILE_PATH);
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get(), ".txt");
    }

    @org.junit.jupiter.api.Test
    void getExtension_severalPoints()
    {
        Optional<String> result = DataSt.getExtension("fileWithSeveral.point.txt");
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(result.get(), ".txt");
    }

    @org.junit.jupiter.api.Test
    void getExtension_invalid()
    {
        Optional<String> result = DataSt.getExtension("invalidStrWithoutPoint");
        Assertions.assertTrue(result.isEmpty());
    }

    @org.junit.jupiter.api.Test
    void getFileNameWithoutExt()
    {
        String result = DataSt.getFileNameWithoutExt(FILE_PATH);
        Assertions.assertEquals(result, "TestFile");
    }

    @org.junit.jupiter.api.Test
    void openFile_file(){
        DataSt.openFile(FILE_PATH);
    }

    @org.junit.jupiter.api.Test
    void openFile_invalid(){
        DataSt.openFile("invalidStr");
    }
    @org.junit.jupiter.api.Test
    void openFile_null(){
        DataSt.openFile(null);
    }

    @org.junit.jupiter.api.Test
    void openFile_folder(){
        DataSt.openFile(FOLDER_PATH);
    }
}