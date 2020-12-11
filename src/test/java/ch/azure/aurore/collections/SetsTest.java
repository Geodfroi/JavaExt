package ch.azure.aurore.collections;

import ch.azure.aurore.collections.Sets;

import java.util.Set;

class SetsTest {

    Set<Integer> intSetA = Set.of(1,2,3);
    Set<Integer> intSetB = Set.of(1,2,3);
    Set<Integer> intSetC = Set.of(1,2,4,6);

    @org.junit.jupiter.api.Test
    void equal_byValues() {
        assert Sets.equals(intSetA, intSetB);
    }

    @org.junit.jupiter.api.Test
    void equal_differentSets() {
        assert !Sets.equals(intSetA, intSetC);
    }

    @org.junit.jupiter.api.Test
    void equal_nullValue() {
        if (Sets.equals(intSetA, null))
            throw new AssertionError();
    }
}