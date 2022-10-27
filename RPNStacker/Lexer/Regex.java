package stacker.rpn.lexer;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Regex {

    public static boolean checkOP(String token) {
        Pattern res = res.compile("[+-/\\*]$", Pattern.CASE_INSENSITIVE);
        return res.matcher(token).matches();
    }
	public static boolean checkNUM(String token) {
        Pattern res = res.compile("-?\\d+$", Pattern.CASE_INSENSITIVE);
        return res.matcher(token).matches();
    }
}