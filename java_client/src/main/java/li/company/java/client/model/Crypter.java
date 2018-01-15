package li.company.java.client.model;

import java.util.Base64;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Crypter {

    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Pattern BASE64_REGEX = Pattern.compile("^(?:[A-Za-z0-9+\\/]{4})*(?:[A-Za-z0-9+\\/]{2}==|[A-Za-z0-9+\\/]{3}=)?$");
    private static final Pattern VALID_BASE64_REGEX = Pattern.compile("[AQgw]==$");
    private static final Pattern CHAR_REGEX = Pattern.compile("^[…♥>ω<\\n\\r:;@$~=*“”\"'’\\/?!+\\-0-9().,a-zA-Z ]+$");
    private static final Pattern ENCODE_REGEX = Pattern.compile("^[=@0-9a-zA-Z]+$");
    private static final Pattern START_REGEX = Pattern.compile("^[<~*\"'\\-0-9(a-zA-Z]+$");

    public static JSONArray crackB64VigenereCypher(String text, long start, long stop) throws JSONException {
        long index = start;
        JSONArray keysFound = new JSONArray();
        do {
            String lastKey = getKey(index);
            String lastResult = VigenereCipher.decrypt(text, lastKey);
            String decodedB64;
            if (lastResult.substring(lastResult.length() - 2, lastResult.length()).equals("==")) {
                if (VALID_BASE64_REGEX.matcher(lastResult).find()) {
                    decodedB64 = new String(Base64.getDecoder().decode(lastResult));
                    if (isValidDecode(decodedB64)) {
                        keysFound.put(new JSONObject()
                                .put("key", lastKey)
                                .put("value", decodedB64));
                    }
                }
            } else {
                if (BASE64_REGEX.matcher(lastResult).find()) {
                    decodedB64 = new String(Base64.getDecoder().decode(lastResult));
                    if (isValidDecode(decodedB64)) {
                        keysFound.put(new JSONObject()
                                .put("key", lastKey)
                                .put("value", decodedB64));
                    }
                }
            }
            index++;
        } while (index < stop);

        return keysFound;
    }

    private static boolean isValidDecode(String decodedB64) {
        if (CHAR_REGEX.matcher(decodedB64).find()) {
            if (hasWhitespace(decodedB64)) {
                if (START_REGEX.matcher(String.valueOf(decodedB64.charAt(0))).find()) {
                    return (decodedB64.split("\"").length - 1) % 2 == 0;
                }
            } else {
                if (ENCODE_REGEX.matcher(decodedB64).find()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean hasWhitespace(String text) {
        return Pattern.compile("\\s").matcher(text).find();
    }

    private static String getKey(long n) {
        return (n >= 26 ? getKey((n / 26) - 1) : "") + CHARSET.charAt((int) (n % 26));
    }
}
