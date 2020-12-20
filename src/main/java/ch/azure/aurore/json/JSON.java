package ch.azure.aurore.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSON {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String toJSON(Object val) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(val);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object fromJSON(Class<?> aClass, String str){
        try {
            return mapper.readValue(str, aClass);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
