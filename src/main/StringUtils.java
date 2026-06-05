package main;

/**
 * Utility class providing string manipulation methods.
 * Offers common string operations used throughout the application.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public final class StringUtils {
    
    private StringUtils() {
        throw new AssertionError("Utility class cannot be instantiated");
    }
    
    /**
     * Replaces a substring within a string with another substring.
     * 
     * @param string the original string
     * @param beginIndex the starting index of the substring to replace (inclusive)
     * @param endIndex the ending index of the substring to replace (exclusive)
     * @param replaceSubstring the string to insert in place of the substring
     * @return a new string with the substring replaced
     * @throws IllegalArgumentException if indices are invalid
     */
    public static String replaceSubstring(String string, int beginIndex, int endIndex, String replaceSubstring) {
        validateIndices(string, beginIndex, endIndex);
        
        String beforeSubstring = string.substring(0, beginIndex);
        String afterSubstring = string.substring(endIndex);
        
        return beforeSubstring + replaceSubstring + afterSubstring;
    }

    /**
     * Validates that the provided indices are within valid bounds.
     * @param string the string to validate indices against
     * @param beginIndex the starting index
     * @param endIndex the ending index
     * @throws IllegalArgumentException if indices are invalid
     */
    private static void validateIndices(String string, int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > string.length()) {
            throw new IllegalArgumentException(
                String.format("Invalid indices: beginIndex=%d, endIndex=%d, string length=%d", 
                    beginIndex, endIndex, string.length())
            );
        }
        if (beginIndex > endIndex) {
            throw new IllegalArgumentException("beginIndex cannot be greater than endIndex");
        }
    }
}
