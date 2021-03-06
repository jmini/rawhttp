package rawhttp.core;

import java.util.OptionalInt;

/**
 * A utility class containing static methods which can check what kind of field-value a String may have.
 * <p>
 * VCHAR is defined in <a href="https://tools.ietf.org/html/rfc5234#appendix-B.1">RFC-5234</a>.
 * <p>
 * token and obs-text are defined on <a href="https://tools.ietf.org/html/rfc7230#section-3.2.6">Section 3.2.6</a>
 * of the HTTP/1.1 specification.
 */
public final class FieldValues {

    // character as int can be used as index in this table to check if the character may appear in a token
    private static final boolean[] TOKEN_CHARS = new boolean[]{
            false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, true, false, true, true, true, true, true,
            false, false, true, true, false, true, true, false, true, true, true, true, true, true, true,
            true, true, true, false, false, false, false, false, false, false, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, false, false, false, true, true, true, true, true, true, true, true,
            true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
            true, true, true, true, true, false, true, false, true, false, false
    };

    /**
     * Check if the given value is allowed in a token field-value.
     * <p>
     * If it is, an empty value is returned, otherwise, the index of the first character that is not allowed in a token
     * is returned.
     *
     * @param value to check
     * @return the index of the first character in the given value which is not allowed in a token field-value, if any.
     */
    public static OptionalInt indexOfNotAllowedInTokens(String value) {
        final char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!isAllowedInTokens(c)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * @param c character
     * @return true if the given char is allowed in token field-values, false otherwise.
     */
    public static boolean isAllowedInTokens(char c) {
        return c < TOKEN_CHARS.length && TOKEN_CHARS[c];
    }

    /**
     * Check if the given value is allowed in a header field value.
     * <p>
     * If it is, an empty value is returned, otherwise, the index of the first character that is not allowed
     * is returned.
     *
     * @param value to check
     * @return the index of the first character in the given value which is not allowed in a VCHAR field-value, if any.
     */
    public static OptionalInt indexOfNotAllowedInHeaderValue(String value) {
        final char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (!isAllowedInHeaderValue(c)) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }

    /**
     * @param c character
     * @return true if the given char is allowed in VCHAR field-values, false otherwise.
     */
    public static boolean isAllowedInVCHARs(char c) {
        return c >= 0x21 && c <= 0x7e;
    }

    /**
     * @param c character
     * @return true if the given char is allowed in obs-text, false otherwise.
     */
    public static boolean isAllowedInObsText(char c) {
        return c >= 0x80 && c <= 0xff;
    }

    /**
     * @param c character
     * @return true if the given char is allowed in a header value, false otherwise.
     */
    public static boolean isAllowedInHeaderValue(char c) {
        return c == ' ' || c == '\t' || isAllowedInVCHARs(c) || isAllowedInObsText(c);
    }
}
