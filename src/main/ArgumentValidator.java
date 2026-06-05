package main;

import argument.Argument;

/**
 * Validates logical arguments to ensure they are well-formed formulas.
 * Encapsulates all validation logic and error handling for argument validation.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class ArgumentValidator {
    private static final String WFF_ERROR_MESSAGE = "ARGUMENT IS NOT A WELL-FORMED-FORMULA (WFF)";
    
    /**
     * Validates that the given argument is a well-formed formula.
     * @param argument the argument to validate
     * @throws IllegalArgumentException if the argument is not a well-formed formula
     */
    public void validate(Argument argument) {
        if (!argument.cleanAndCheckIfArgumentIsWellFormedFormula()) {
            throw new IllegalArgumentException(WFF_ERROR_MESSAGE);
        }
    }
    
    /**
     * Removes double negations from the given argument.
     * @param argument the argument to process
     */
    public void removeDoubleNegations(Argument argument) {
        argument.removeDoubleNegations();
    }
}
