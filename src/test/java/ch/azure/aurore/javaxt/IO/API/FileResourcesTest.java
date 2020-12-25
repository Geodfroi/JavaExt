package ch.azure.aurore.javaxt.IO.API;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileResourcesTest {

    @Test
    void name() {
        String str =  FileResources.getResourceText("settings.json");
        Assertions.assertEquals(str, "{}");
    }
}