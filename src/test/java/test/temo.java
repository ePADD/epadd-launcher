package test;

import java.util.regex.Pattern;

/**
 * Created by hangal on 6/20/16.
 */
public class temo {
    public static void main (String args[]) {
        String regex = "I check for (.*)( *)(\\d*) messages on the page";
        Pattern p = Pattern.compile(regex);
        System.out.println (Pattern.matches (regex, "I check for 320 messages on the page"));
    }
}
