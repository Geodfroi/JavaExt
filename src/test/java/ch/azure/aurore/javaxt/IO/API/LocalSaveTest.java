package ch.azure.aurore.javaxt.IO.API;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class LocalSaveTest {
    private static final String PROPERTY_NAME = "distance";

    @Test
    void name() {
        LocalSave.getInstance().set(PROPERTY_NAME, 56);
        Optional<Integer> result = LocalSave.getInstance().getInteger(PROPERTY_NAME);
        assert result.isPresent();
        Assertions.assertEquals(result.get(), 56);

        LocalSave.getInstance().setIntegers("aaa", 23, 42);
    }
}