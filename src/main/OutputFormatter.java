package main;

/**
 * Handles formatting and display of output for the argument processing results.
 * Provides methods to print the validity of arguments in a user-friendly format.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class OutputFormatter {
    private static final String VALID_MESSAGE = "✓ The argument is valid.";
    private static final String INVALID_MESSAGE = "X The argument is invalid.";

    /**
     * Prints the validity of an argument to the console.
     * Displays a checkmark (✓) if the argument is valid, or an X if it is invalid.
     * 
     * @param valid true if the argument is valid, false otherwise
     */
    public void printArgumentValidity(boolean valid) {
        System.out.println(valid ? VALID_MESSAGE : INVALID_MESSAGE);
    }
}
