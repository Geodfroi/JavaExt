package ch.azure.aurore.collections;

import java.util.Set;

public class Sets {
    public static <T> boolean equals(Set<T> set1, Set<T> set2) {

        if(set1 == null || set2 ==null)
            return false;

        if (set1 == set2)
            return true;

        if(set1.size()!=set2.size())
            return false;

        return set1.containsAll(set2);
    }
}
