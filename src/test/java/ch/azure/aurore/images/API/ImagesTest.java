package ch.azure.aurore.images.API;

import ch.azure.aurore.IO.API.Disk;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

class ImagesTest {

    private static final String IMAGE_PATH = "testImg.png";
    private static final String FILE_EXPORT_PATH ="exportFolder/exportFile.png";
    private static Optional<byte[]> bytesArray;

    @org.junit.jupiter.api.BeforeAll
    static void beforeAll() {
        File file = new File(IMAGE_PATH);
        bytesArray = Images.toByteArray(file);
    }

    @org.junit.jupiter.api.Test
    void filesToByte() {
        assert bytesArray.isPresent();
    }

    @org.junit.jupiter.api.Test
    void bytesToBufferedImage(){
        Optional<BufferedImage> result =  Images.toImage(bytesArray.get());
        assert (result.isPresent() && result.get().getHeight() != 0);
    }

    @org.junit.jupiter.api.Test
    void exportFile(){
        File f = Images.toFile(bytesArray.get(), FILE_EXPORT_PATH);
        assert (f != null);
    }
}