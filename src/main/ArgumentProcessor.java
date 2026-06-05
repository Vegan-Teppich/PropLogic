package main;

import argument.Argument;
import argument.TruthTable;

/**
 * This class is responsible for processing the argument provided by the user.
 * It orchestrates the entire workflow:
 * 1. Getting the argument from the user via the InputHandler.
 * 2. Validating the argument to ensure it is a well-formed formula.
 * 3. Removing double negations from the argument.
 * 4. Generating a truth table for the argument.
 * 5. Displaying the truth table and the validity of the argument to the user.
 * 
 * This class serves as the central orchestration point for argument processing,
 * ensuring a smooth and cohesive user experience.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class ArgumentProcessor {
    private final InputHandler inputHandler;
    private final ArgumentValidator validator;
    private final OutputFormatter outputFormatter;

    /**
     * Constructs an ArgumentProcessor with all required dependencies.
     */
    public ArgumentProcessor() {
        this.inputHandler = new InputHandler();
        this.validator = new ArgumentValidator();
        this.outputFormatter = new OutputFormatter();
    }

    /**
     * Processes an argument through the entire workflow.
     * Gets user input, validates it, removes double negations, generates a truth table, and displays the results.
     * 
     * @throws IllegalArgumentException if the argument is not a well-formed formula
     */
    public void process() {
        Argument argument = getArgumentFromUser();
        validateArgument(argument);
        processArgument(argument);
        displayResults(argument);
    }

    /**
     * Gets the argument from the user via the InputHandler.
     * @return the Argument object provided by the user
     */
    private Argument getArgumentFromUser() {
        return inputHandler.getArgumentFromUser();
    }

    /**
     * Validates the given argument.
     * @param argument the argument to validate
     * @throws IllegalArgumentException if the argument is not a well-formed formula
     */
    private void validateArgument(Argument argument) {
        validator.validate(argument);
    }

    /**
     * Processes the argument by removing double negations.
     * @param argument the argument to process
     */
    private void processArgument(Argument argument) {
        validator.removeDoubleNegations(argument);
    }

    /**
     * Displays the results by generating and printing a truth table and argument validity.
     * @param argument the argument to analyze
     */
    private void displayResults(Argument argument) {
        TruthTable truthTable = new TruthTable(argument);
        truthTable.print();
        System.out.println();
        outputFormatter.printArgumentValidity(truthTable.isArgumentValid());
    }
}
