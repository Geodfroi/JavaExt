package ch.azure.aurore.Strings;

public class Strings {

    public static String toFirstLower(String str){
        char c[] = str.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        return new String(c);
    }

    public static String toFirstUpper(String str){

        char c[] = str.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
}
