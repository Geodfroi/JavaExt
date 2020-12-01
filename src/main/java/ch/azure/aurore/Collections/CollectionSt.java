package ch.azure.aurore.Collections;
import java.util.Collection;

public class CollectionSt {

    public static String toString(Collection<?> collection) {
        return toString(collection, ", ");
    }

    public static String toString(Collection<?> a, String separator) {

        if (a == null)
            return "";
        int size = a.size();
        if (size == 0) {
            return "";
        }

        StringBuilder str = new StringBuilder();
        int count = 0;
        for (var item:a) {
            str.append(item.toString());
            if (count < size -1)
                str.append(separator);
            count ++;
        }

        return  str.toString();
    }
}
