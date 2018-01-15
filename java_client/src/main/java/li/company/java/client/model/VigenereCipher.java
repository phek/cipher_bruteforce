package li.company.java.client.model;

public class VigenereCipher {

    public static String encrypt(String text, String key) {
        int[] encodeKey = filterKey(key);
        return crypt(text, encodeKey);
    }

    public static String decrypt(String text, String key) {
        int[] decodeKey = getDecryptKey(filterKey(key));
        return crypt(text, decodeKey);
    }

    private static String crypt(String text, int[] key) {
        String output = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            int code = text.codePointAt(i);
            if (isUppercase(code)) {
                output += fromCharCode((code - 65 + key[j % key.length]) % 26 + 65);
                j++;
            } else if (isLowercase(code)) {
                output += fromCharCode((code - 97 + key[j % key.length]) % 26 + 97);
                j++;
            } else {
                output += text.charAt(i);
            }
        }
        return output;
    }

    private static char fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length).charAt(0);
    }

    // Tests whether the specified character code is a letter.
    private static boolean isLetter(int code) {
        return isUppercase(code) || isLowercase(code);
    }

    // Tests whether the specified character code is an uppercase letter.
    private static boolean isUppercase(int code) {
        return 65 <= code && code <= 90;  // 65 is character code for 'A'. 90 is 'Z'.
    }

    // Tests whether the specified character code is a lowercase letter.
    private static boolean isLowercase(int code) {
        return 97 <= code && code <= 122;  // 97 is character code for 'a'. 122 is 'z'.
    }

    private static int[] getDecryptKey(int[] key) {
        for (int i = 0; i < key.length; i++) {
            key[i] = (26 - key[i]) % 26;
        }
        return key;
    }

    /*
    * Returns an array of numbers, each in the range [0, 26), representing the given key.
    * The key is case-insensitive, and non-letters are ignored.
    * Examples:
    * - filterKey("AAA") = [0, 0, 0].
    * - filterKey("abc") = [0, 1, 2].
    * - filterKey("the $123# EHT") = [19, 7, 4, 4, 7, 19].
     */
    private static int[] filterKey(String key) {
        int[] result = new int[key.length()];
        for (int i = 0; i < key.length(); i++) {
            int code = key.codePointAt(i);
            if (isLetter(code)) {
                result[i] = ((code - 65) % 32);
            }
        }
        return result;
    }
}
