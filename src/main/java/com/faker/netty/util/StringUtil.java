package com.faker.netty.util;

/**
 * Created by faker on 18/4/12.
 */
public class StringUtil {

    public static String capitalize(String str) {
        char[] ch = str.toCharArray();
        for (int i = 1; i < ch.length; i++) {
            if (ch[i] >= 'A' && ch[i] <= 'Z') {
                ch[i] = (char) (ch[i] + 32);
            }
        }

        return String.valueOf(ch);

    }

}
