package main;

import java.util.Scanner;

/**
 * Handles collection of premise and conclusion counts from user input.
 * Encapsulates the logic for prompting and parsing user input for argument dimensions.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class ArgumentCountCollector {
    private static final String PREMISE_PROMPT = "How many premises does your argument have?";
    private static final String CONCLUSION_PROMPT = "How many conclusions does your argument have?";
    
    private final Scanner scanner;
    
    /**
     * Constructs an ArgumentCountCollector with the given scanner.
     * @param scanner the Scanner instance for reading user input
     */
    public ArgumentCountCollector(Scanner scanner) {
        this.scanner = scanner;
    }
    
    /**
     * Collects the number of premises and conclusions from the user.
     * @return an array containing [numberOfPremises, numberOfConclusions]
     */
    public int[] collectArgumentCounts() {
        System.out.println(PREMISE_PROMPT);
        int numberOfPremises = Integer.parseInt(scanner.nextLine());
        
        System.out.println(CONCLUSION_PROMPT);
        int numberOfConclusions = Integer.parseInt(scanner.nextLine());
        
        return new int[]{numberOfPremises, numberOfConclusions};
    }
}
