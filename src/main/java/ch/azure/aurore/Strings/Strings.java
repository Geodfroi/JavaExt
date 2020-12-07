package ch.azure.aurore.Strings;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Strings {

    public static String toFirstLower(String str){
        char[] c = str.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String toFirstUpper(String str){

        char[] c = str.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }

    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty() || str.isBlank();
    }

    public static String camel(String str){
        if (isNullOrEmpty(str))
            return "";

        StringBuilder sb = new StringBuilder();
        char[] array = str.toCharArray();
        boolean setNextToUpper = false;

        for (int n = 0; n < str.length(); n++) {
            char ch = array[n];
            if (Character.isWhitespace(ch)){
                if (n !=0){
                    setNextToUpper = true;
                }
            }else {
                if (n == 0){
                    sb.append(ch);
                }else {
                    if (setNextToUpper){
                        sb.append(Character.toUpperCase(ch));
                        setNextToUpper = false;
                    }else{
                        sb.append(ch);
                    }
                }
            }
        }
        return sb.toString();
    }



    public static String toString(Stream<?> st) {
        return toString(st, ", ");
    }
    public static String toString(Collection<?> st) {
        return toString(st, ", ");
    }

    public static String toString(Stream<?> st, String separator) {
        List<?> list = st.collect(Collectors.toList());
        return toString(list, separator);
    }

    public static String toString(Collection<?> list, String separator) {

        if (list == null)
            return "";

        StringBuilder str = new StringBuilder();
        boolean hasFormerItem = false;

        for (var item:list) {
            if (item == null)
                continue;
            if (item instanceof String){
                String s = (String)item;
                if (s.isEmpty() || s.isBlank())
                    continue;
            }
            if (hasFormerItem){
                str.append(separator);
            }
            str.append(item.toString());
            hasFormerItem = true;
        }

        return  str.toString();
    }

    public static String unCamel(String str){
        if (isNullOrEmpty(str))
            return "";

        StringBuilder sb = new StringBuilder();
        var array = str.toCharArray();
        for (int n = 0; n < str.length(); n++)
        {
            char ch = array[n];
            if (n == 0) {
                sb.append(ch);
            }
            else {
                if (Character.isUpperCase(ch)){
                    sb.append(' ');
                    sb.append(Character.toLowerCase(ch));
                }else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }
}