package ch.azure.aurore.javaxt.IO.API;

import org.apache.commons.io.IOUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 *https://mkyong.com/java/java-read-a-file-from-resources-folder/
 */
public class FileResources {

    /**
     * @param pathStr the file path inside the resource folder. The method throws a runtime exception if the file is absent or can't be read.
     * @return The text content of the file.
     */
    public static String getResourceText(String pathStr){
        String txt;
        try {
            //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(file);
            InputStream inputStream = FileResources.class.getClassLoader().getResourceAsStream(pathStr);
            if (inputStream == null)
                throw new FileNotFoundException();
            txt = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(txt);
        } catch (IOException e) {
            throw new RuntimeException("can't access resource file at [" + pathStr + "]");
        }
        return txt;
    }
}