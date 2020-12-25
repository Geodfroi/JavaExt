package ch.azure.aurore.javaxt.images.API;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

class ImagesTest {

    private static final String IMAGE_PATH = "testImg.png";
    private static final String FILE_EXPORT_PATH ="exportFolder/exportFile.png";
    private static byte[] bytesArray;

    @org.junit.jupiter.api.BeforeAll
    static void beforeAll() throws IOException {

        bytesArray = Objects.requireNonNull(ImagesTest.class.getClassLoader().
                getResourceAsStream(IMAGE_PATH)).readAllBytes();
    }

    @org.junit.jupiter.api.Test
    void bytesToBufferedImage(){
        Optional<BufferedImage> result =  Images.toImage(bytesArray);
        assert (result.isPresent() && result.get().getHeight() != 0);
    }

    @org.junit.jupiter.api.Test
    void exportFile(){
        File f = Images.toFile(bytesArray, FILE_EXPORT_PATH);
        assert (f != null);
    }
}