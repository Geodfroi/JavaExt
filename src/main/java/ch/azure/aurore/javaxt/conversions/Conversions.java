package ch.azure.aurore.javaxt.conversions;

import ch.azure.aurore.javaxt.strings.Strings;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Conversions {

    private static final String DEFAULT_SEPARATOR = ",";

    public static boolean toBoolean(String s) {
        if (Strings.isNullOrEmpty(s))
            return false;

        return s.equals("1") || s.equals("true");
    }

    public static boolean toBoolean(int val) {
        return val > 0;
    }

    public static double[] toDoubleArray(String[] array) {
        if (array == null)
            return new double[0];
       return Arrays.stream(array).
               mapToDouble(Double::parseDouble).toArray();
    }

    private static int toInt(boolean val) {
        return val ? 1: 0;
    }

    public static int[] toIntArray(String[] array) {
        if (array == null)
            return new int[0];
        return Arrays.stream(array).mapToInt(Integer::parseInt).toArray();
    }

    //region toString
    public static String toString(Stream<?> stream) {
        return toString(stream, null, null);
    }

    public static String toString(Stream<?> stream, String separator) {
        return toString(stream, null, separator);
    }

    public static <T> String toString(Stream<T> stream, Comparator<T> comparator) {
        return toString(stream, comparator, null);
    }

    public static <T> String toString(Stream<T> stream, Comparator<T> comparator, String separator) {

        separator = separator != null ? separator : DEFAULT_SEPARATOR;

        if (stream == null)
            return "";
        List<T> list;
        if (comparator == null)
            list = stream.collect(Collectors.toList());
        else
            list = stream.sorted(comparator).collect(Collectors.toList());

        StringBuilder str = new StringBuilder();
        boolean hasFormerItem = false;

        for (T item:list) {
            if (item == null)
                continue;
            if (item instanceof String){
                String s = (String)item;
                if (Strings.isNullOrEmpty(s))
                    continue;
            }
            if (hasFormerItem){
                str.append(separator);
            }
            str.append(item.toString());
            hasFormerItem = true;
        }

        return str.toString();
    }

    public static <T> String toString(Collection<T> list) {
        return toString(list.stream(), null,null);
    }

    public static <T> String toString(Collection<T> list, Comparator<T> comparator) {
        return toString(list.stream(), comparator, null);
    }

    public static <T> String toString(Collection<T> list, String separator) {
        return toString(list.stream(), null, separator);
    }

    public static String toString(String[] array) {
        if (array == null)
            return null;
        return toString(Arrays.stream(array), null, null);
    }

    public static String toString(String[] array, String separator) {
        if (array == null)
            return null;
        return toString(Arrays.stream(array), null, separator);
    }

    public static String toString(int[] array, String separator) {
        if (array == null)
            return null;
        return toString(Arrays.stream(array).boxed(), null, separator);
    }

    public static String toString(int[] array) {
        if (array == null)
            return null;
        return toString(Arrays.stream(array).boxed(), null,null);
    }

    public static String toString(double[] array) {
        if (array == null)
            return null;
        return toString(Arrays.stream(array).boxed());
    }

    //endregion
}

////    /**
////     * @param str A string representing the parsed collection.
////     * @return The collection as an array or list; or the original parameter if it doesn't represent a collection.
////     */
////    public static Object parseCollectionFromString(String str) {
////
////        if (!Strings.isNullOrEmpty(str)){
////            if (str.startsWith(STRING_ARRAY_MARKER)){
////                return str.substring(STRING_ARRAY_MARKER.length(), str.length()-1).
////                        split(ARRAY_SEPARATOR);
////            }else if(str.startsWith(INT_ARRAY_MARKER)){
////                return Arrays.stream(str.substring(INT_ARRAY_MARKER.length(), str.length()-1).
////                        split(ARRAY_SEPARATOR)).
////                        mapToInt(s -> Integer.parseInt(s)).toArray();
////            }
////        }
////        return str;
////    }
////
////    public static String parseCollectionToString(Object value) {
////
////      if (value instanceof int[]) {
////          return INT_ARRAY_MARKER + Strings.concatenate((int[])value, ARRAY_SEPARATOR) + ")";
////      }
////      if (value instanceof String[]){
////          return STRING_ARRAY_MARKER + Strings.concatenate((String[])value, ARRAY_SEPARATOR) + "}";
////      }
////    throw new RuntimeException("value [" + value + "] is not a valid collection");
////    }
