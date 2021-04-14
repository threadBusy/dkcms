package cc.dkcms.cms.common.util;

import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;

public class IntegerUtils {

    private static final String dict = "qsa80pxu3bv6yrgd75c2m14winj9hzekt";

    public static String encode(int a) {
        if (a < 0) {
            return "-" + (encode(0 - a));
        }

        LinkedList<Character> stringBuilder = new LinkedList<>();
        int                   length        = dict.length();
        while ((a / length) >= length) {
            stringBuilder.addFirst(dict.charAt(a % length));
            a = a / length;
        }
        stringBuilder.addFirst(dict.charAt((a % length)));
        stringBuilder.addFirst(dict.charAt((a / length)));
        return StringUtils.join(stringBuilder, "");
    }

    public static int decode(String a) {
        if ("-".equals(a.substring(0, 1))) {
            return 0 - decode(a.substring(1));
        }
        int intVal = 0;
        for (int i = a.length(); i > 0; i--) {
            intVal += Math.pow(dict.length(), i - 1) * dict.indexOf(a.charAt(a.length() - i));
        }
        return intVal;

    }
}
