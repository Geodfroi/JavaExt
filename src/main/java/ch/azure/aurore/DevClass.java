package ch.azure.aurore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DevClass {

    public static void main(String[] args) {

        String txt = "[Empty]   ";
        String regex = "\\[Empty] *";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txt);
        System.out.println(matcher.matches());
    }
}

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