package main;

import argument.Argument;

import java.util.Scanner;

/**
 * This class is responsible for handling user input related to the argument.
 * It prompts the user for the number of premises and conclusions, and then collects the premises and conclusions themselves.
 * The user can cancel the input process at any time by typing "back".
 * The collected premises and conclusions are then used to create an Argument object.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class InputHandler {
    private static final String CANCEL_PHRASE = "back";
    private static final String PREMISE_LABEL = "premise";
    private static final String CONCLUSION_LABEL = "conclusion";
    
    private final Scanner scanner;
    private final ArgumentCountCollector countCollector;
    private boolean inputComplete;
    private boolean inputCanceled;

    /**
     * Constructs an InputHandler instance.
     * Initializes the Scanner and ArgumentCountCollector for reading user input.
     */
    public InputHandler() {
        this.scanner = new Scanner(System.in);
        this.countCollector = new ArgumentCountCollector(scanner);
        this.inputComplete = false;
        this.inputCanceled = false;
    }

    /**
     * Prompts the user to input premises and conclusions for an argument.
     * The user can type "back" to cancel and restart the input process.
     * 
     * @return an Argument object created from the user's input
     */
    public Argument getArgumentFromUser() {
        while (!inputComplete) {
            int[] counts = countCollector.collectArgumentCounts();
            int numberOfPremises = counts[0];
            int numberOfConclusions = counts[1];

            String[] premises = collectPremises(numberOfPremises);
            if (inputCanceled) {
                resetInputState();
                continue;
            }

            String[] conclusions = collectConclusions(numberOfConclusions, numberOfPremises);
            if (inputCanceled) {
                resetInputState();
                continue;
            }

            inputComplete = true;
            return new Argument(premises, conclusions);
        }
        
        return null;
    }

    /**
     * Collects premises from the user.
     * @param numberOfPremises the number of premises to collect
     * @return an array of premise strings
     */
    private String[] collectPremises(int numberOfPremises) {
        String[] premises = new String[numberOfPremises];
        for (int i = 0; i < numberOfPremises; i++) {
            premises[i] = collectPropositionFromUser(i + 1, PREMISE_LABEL);
            if (inputCanceled) {
                return premises;
            }
        }
        return premises;
    }

    /**
     * Collects conclusions from the user.
     * @param numberOfConclusions the number of conclusions to collect
     * @param numberOfPremises the number of premises (used for numbering)
     * @return an array of conclusion strings
     */
    private String[] collectConclusions(int numberOfConclusions, int numberOfPremises) {
        String[] conclusions = new String[numberOfConclusions];
        for (int i = 0; i < numberOfConclusions; i++) {
            conclusions[i] = collectPropositionFromUser(i + 1, CONCLUSION_LABEL);
            if (inputCanceled) {
                return conclusions;
            }
        }
        return conclusions;
    }

    /**
     * Collects a single proposition from the user with the given label.
     * @param index the 1-based index of the proposition
     * @param label the type label (e.g., "premise" or "conclusion")
     * @return the proposition string entered by the user
     */
    private String collectPropositionFromUser(int index, String label) {
        System.out.printf("Tell me the %d. %s%n", index, label);
        String input = scanner.nextLine().trim();
        
        if (input.equalsIgnoreCase(CANCEL_PHRASE)) {
            inputCanceled = true;
        }
        
        return input;
    }

    /**
     * Resets the input state for a new input attempt.
     */
    private void resetInputState() {
        inputCanceled = false;
        inputComplete = false;
    }
}
