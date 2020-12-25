package ch.azure.aurore.javaxt.json.API;

import ch.azure.aurore.javaxt.strings.Strings;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.IOException;
import java.util.*;


/**
 * https://www.baeldung.com/jackson-collection-array
 */
public class JSON {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJSON(Object val) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(val);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static <T> T readValue(Class<T> clazz, String str) {

        if (Strings.isNullOrEmpty(str))
            return null;

        try {
            if (Collection.class.isAssignableFrom(clazz))
                throw new IllegalArgumentException("Can't parse str collection using [fromJSON], use [loadCollection] instead");

//            Pattern p = Pattern.compile("^\\[.*]$");
//            var isArrayOrCollection = p.matcher(str).matches();
//            System.out.println("isArray: " + isArrayOrCollection);
//           d.");

            // for array or values
            return mapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <TInternal> Set<TInternal> readSet(Class<TInternal> type, String str) {
        try {
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(Set.class, type);
            return mapper.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    public static <TInternal> List<TInternal> readList(Class<TInternal> type, String str) {
        try {
            CollectionType javaType = mapper.getTypeFactory()
                    .constructCollectionType(List.class, type);
            return mapper.readValue(str, javaType);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static Object readItem(Class<?> clazz, String str) {
        try {
            return mapper.readValue(str, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}

