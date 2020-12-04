package ch.azure.aurore.images.API;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ImagesTest {

    private static final String IMAGE_PATH = "testImg.png";

    @org.junit.jupiter.api.Test
    void filesToByte() {
        File file = new File(IMAGE_PATH);
        Optional<byte[]> res = Images.toByteArray(file);
        assert res.isPresent();
    }

    @org.junit.jupiter.api.Test
    void bytesToBufferedImage(){
        File file = new File(IMAGE_PATH);
        Optional<byte[]> res = Images.toByteArray(file);
        assert res.isPresent();

        Optional<BufferedImage> result =  Images.toImage(res.get());
        assert (result.isPresent() && result.get().getHeight() != 0);
    }
}