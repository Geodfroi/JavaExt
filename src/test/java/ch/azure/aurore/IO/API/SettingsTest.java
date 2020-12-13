package ch.azure.aurore.IO.API;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    public static final String PROPERTY_NAME = "count";

    @Test
    void getSettings() {
        Optional<List<Integer>> d =  Settings.getInstance().getIntegers("invalid");
        assert d.isEmpty();
    }

    @Test
    void getSettings_valid() {
        Optional<Integer> d =  Settings.getInstance().getInteger(PROPERTY_NAME);
        assert d.isPresent();
        Assertions.assertEquals(d.get(), 7);
    }
}