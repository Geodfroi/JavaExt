package ch.azure.aurore.javaxt;

import ch.azure.aurore.javaxt.strings.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevClass {
    public static void main(String[] args) {

        String label = "cats";
        if (Strings.isNullOrEmpty(label))
            return;

        String a = label.substring(0, label.length()-1);
        System.out.println(a);
        String b = label.substring(label.length()-1);
        System.out.println(b);

        String regex ="^.*\\b" + label + "?\\b.*$";
        System.out.flush();
        System.out.println(regex);
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);

        var txt = "cat";
        Matcher matcher = pattern.matcher(txt);
            if (matcher.matches()){
                System.out.println("matches");
            }
        }
}
//
//        String txt = "[Empty]   ";
//        String regex = "\\[Empty] *";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(txt);
//        System.out.println(matcher.matches());


//    public static void main(String[] args) {
//        String text = "The sands are fine";
//        //String text = "sand";
//      //  String regex =".*\bwolf\b.*";
//        String regex ="^.*\\bsand[sx]?\\b.*$";
//        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//
//        Matcher matcher = pattern.matcher(text);
//        System.out.println(matcher.matches());
//    }