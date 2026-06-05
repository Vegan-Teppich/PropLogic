package main;

/**
 * The main entry point of the application.
 * Initiates the argument processing workflow by delegating to ArgumentProcessor.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class Main {
    
    /**
     * The entry point of the application.
     * Creates an ArgumentProcessor instance and executes the argument processing workflow.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        new ArgumentProcessor().process();
    }
}