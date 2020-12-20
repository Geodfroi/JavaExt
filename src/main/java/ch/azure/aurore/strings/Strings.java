package ch.azure.aurore.strings;

public class Strings {

    public static final String DEFAULT_SEPARATOR = ",";

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

    public static boolean isNullOrEmpty(String str){
        return str == null || str.isEmpty() || str.isBlank();
    }

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