package com.tomclaw.wishlists.util;

/**
 * Created with IntelliJ IDEA.
 * User: Solkin
 * Date: 13.10.13
 * Time: 20:09
 */
public class StringUtil {

    public static final char DEFAULT_ALPHABET_INDEX = '?';

    public static char getAlphabetIndex(String name) {
        for (int c = 0; c < name.length(); c++) {
            char character = name.charAt(c);
            if (Character.isLetterOrDigit(character)) {
                return Character.toUpperCase(character);
            }
        }
        return DEFAULT_ALPHABET_INDEX;
    }
}
