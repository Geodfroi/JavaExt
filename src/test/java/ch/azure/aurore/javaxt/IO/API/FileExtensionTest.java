package ch.azure.aurore.javaxt.IO.API;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

class FileExtensionTest {

    @Test
    void test_file() {
        File file = new File("strange.epub");
        assert FileExtension.getExtension(file) == FileExtension.EPUB;
    }

    @Test
    void test_path() {
        Path path = new File("strange.epub").toPath();
        assert FileExtension.getExtension(path) == FileExtension.EPUB;
    }

    @Test
    void test_isExtension() {
        File file = new File("strange.epub");
        Path path = file.toPath();
        assert FileExtension.isExtension(file, FileExtension.EPUB);
        assert FileExtension.isExtension(path, FileExtension.EPUB);
        assert FileExtension.isExtension(path, FileExtension.EBOOK);
    }
}