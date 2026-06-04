package main;

import argument.Argument;
import argument.TruthTable;

import java.util.Scanner;

public class Main {
    final static Scanner scanner = new Scanner(System.in);
    static boolean inputComplete = false;
    static boolean inputCanceled = false;
    static int numberOfPremises = 0;
    static int numberOfConclusions = 0;
    static Argument arg;

    public static void main(String[] args) {

        arg = input();

        if (!arg.cleanAndCheckIfArgumentIsWellFormedFormula())
            throw new IllegalArgumentException("ARGUMENT IS NOT A WELL-FORMED-FORMULA (WFF)");

        arg.removeDoubleNegations();

        TruthTable tt = new TruthTable(arg);

        tt.print();

        System.out.println();

        printValid(tt.isArgumentValid());

    }

    public static void printValid(boolean valid){
        if (valid) {
            System.out.println("✓ The argument is valid.");
        } else {
            System.out.println("X The argument is invalid.");
        }
    }

    public static Argument input() {
        final String cancelPhrase = "back";

        Argument arg;
        String[] premises = {""};
        String[] conclusions = {""};
        while (!inputComplete) {
            System.out.println("How many premises does your argument have?");
            numberOfPremises = Integer.parseInt(scanner.nextLine());
            System.out.println("How many conclusions does your argument have?");
            numberOfConclusions = Integer.parseInt(scanner.nextLine());

            premises = new String[numberOfPremises];
            conclusions = new String[numberOfConclusions];

            for (int p = 0; p < numberOfPremises + numberOfConclusions; p++) {
                System.out.print("Tell me the ");
                if (p < numberOfPremises) {
                    System.out.print((p + 1) + ".");
                    System.out.println(" premise");
                } else {
                    System.out.print((p-numberOfPremises) + 1 + ".");
                    System.out.println(" conclusion");
                }
                String input = scanner.nextLine();
                if (input.trim().equalsIgnoreCase(cancelPhrase)) {
                    inputCanceled = true;
                    break;
                }
                if (p < numberOfPremises) {
                    premises[p] = input;
                } else {
                    conclusions[p - numberOfPremises] = input;
                }
            }
            if (inputCanceled)
                continue;

            inputComplete = true;
        }

        arg = new Argument(premises, conclusions);
        return arg;
    }

    public static String replaceSubstring(String string, int beginIndex, int endIndex, String replaceSubstring) {
        if (beginIndex >= string.length() || endIndex > string.length())
            throw new IllegalArgumentException();
        if (beginIndex > endIndex)
            throw new IllegalArgumentException();
        String substring1 = string.substring(0, beginIndex);
        String substring2 = string.substring(endIndex);

        return substring1 + replaceSubstring + substring2;
    }


}