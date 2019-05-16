package com.zipsecure.secure;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static SecureRandom random = new SecureRandom();

    private static final String ALPHA_CAPS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*_=+-/";
    private static final int LENGTH_PASSWORD = 16;

    /**
     * Method will generate random string based on the parameters
     *
     * @param len the length of the random string
     * @param dic the dictionary used to generate the password
     * @return the random password
     */
    private static char[] generatePassword(int len, String dic) {
        char[] password = new char[len];
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            password[i] = dic.charAt(index);
        }
        return password;
    }

    /**
     * Method will generate random char array automatically
     * @return the random password
     */
    public static char[] autoGeneratePassword() {
        String dic = ALPHA_CAPS + ALPHA + NUMERIC + SPECIAL_CHARS;
        return generatePassword(LENGTH_PASSWORD, dic);
    }
}
