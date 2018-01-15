package li.company.java.client.model;

public class VigenereCipher {

    public static void main(String[] args) {
        System.out.println(decrypt("Kwj ody hhlew szioap", "DSAFDSA"));
    }

    //NOT TESTED. MOST LIKELY DOESN'T WORK YET
    public static String encrypt(String text, final String key) {
        String res = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isLowercase(c)) {
                res += (char) ((c + key.charAt(j) - 2 * 'a') % 26 + 'a');
                j = ++j % key.length();
            } else if (isUppercase(c)) {
                res += (char) ((c + key.charAt(j) - 2 * 'A') % 26 + 'A');
                j = ++j % key.length();
            } else {
                res += c;
            }
        }
        return res;
    }

    public static String decrypt(String text, final String key) {
        String res = "";
        for (int i = 0, j = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isLowercase(c)) {
                res += (char) ((c - key.charAt(j) + 20) % 26 + 'a');
                j = ++j % key.length();
            } else if (isUppercase(c)) {
                res += (char) ((c - key.charAt(j) + 26) % 26 + 'A');
                j = ++j % key.length();
            } else {
                res += c;
            }
        }
        return res;
    }

    // Tests whether the specified character code is an uppercase letter.
    private static boolean isUppercase(char code) {
        return 65 <= code && code <= 90;  // 65 is character code for 'A'. 90 is 'Z'.
    }

    // Tests whether the specified character code is a lowercase letter.
    private static boolean isLowercase(char code) {
        return 97 <= code && code <= 122;  // 97 is character code for 'a'. 122 is 'z'.
    }
}
