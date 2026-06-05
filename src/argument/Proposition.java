package argument;

import compoundProposition.AtomicProposition;
import compoundProposition.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a logical proposition, which can be either a premise or a conclusion.
 * It extends AtomicProposition and adds functionality for managing sub-propositions and tracking parentheses.
 * A proposition is parsed into sub-propositions to enable evaluation of complex logical formulas.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class Proposition extends AtomicProposition {
    private List<SubProposition> subProps;
    private int[] parenthesesCount;
    protected Mode mode;
    protected int propIndex;

    /**
     * Constructs a Proposition with the given string, mode, and index.
     * @param propString the proposition string representation
     * @param mode the mode (PREMISE or CONCLUSION)
     * @param propIndex the index of this proposition
     */
    public Proposition(String propString, Mode mode, int propIndex) {
        super(propString);
        this.mode = mode;
        this.propIndex = propIndex;
        initialize();
    }

    /**
     * Initializes the proposition by cleaning the string, counting parentheses, and extracting sub-propositions.
     */
    private void initialize() {
        this.propString = cleanPropString();
        this.parenthesesCount = countParentheses();
        this.subProps = extractSubProps();
    }

    /**
     * Cleans the proposition string by removing spaces, adding surrounding parentheses if needed, and removing triple negations.
     * @return the cleaned proposition string
     */
    public String cleanPropString() {
        String cleaned = this.propString.replace(" ", "");

        if (cleaned.isEmpty()) {
            return wrapInParentheses(cleaned);
        }

        if (needsParenthesesWrapper(cleaned)) {
            cleaned = wrapInParentheses(cleaned);
        }

        return removeTripleNegations(cleaned);
    }

    /**
     * Checks if a proposition string needs to be wrapped in parentheses.
     * @param propString the proposition string to check
     * @return true if parentheses are needed, false otherwise
     */
    private boolean needsParenthesesWrapper(String propString) {
        boolean startsWithParen = propString.charAt(0) == Operator.OPENING_PARENTHESIS.getSyntax();
        boolean endsWithParen = propString.charAt(propString.length() - 1) == Operator.CLOSING_PARENTHESIS.getSyntax();

        if (startsWithParen && endsWithParen) {
            return false;
        }

        boolean startsWithNegation = propString.charAt(0) == Operator.NEGATION.getSyntax();
        return !(startsWithNegation && !endsWithParen);
    }

    /**
     * Wraps a proposition string in parentheses.
     * @param propString the proposition string to wrap
     * @return the wrapped proposition string
     */
    private String wrapInParentheses(String propString) {
        return Operator.OPENING_PARENTHESIS.getSyntax() + propString + Operator.CLOSING_PARENTHESIS.getSyntax();
    }

    /**
     * Removes triple negations (and higher odd counts) from a proposition string.
     * @param propString the proposition string to process
     * @return the proposition string with triple negations removed
     */
    private String removeTripleNegations(String propString) {
        String tripleNegation = Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax();
        int searchIndex = 0;

        while ((searchIndex = propString.indexOf(tripleNegation, searchIndex)) != -1) {
            propString = propString.substring(0, searchIndex) + propString.substring(searchIndex + 2);
        }

        return propString;
    }

    /**
     * Counts the opening and closing parentheses in the proposition string.
     * @return an array [openingCount, closingCount]
     */
    public int[] countParentheses() {
        int openingCount = 0;
        int closingCount = 0;

        for (char character : propString.toCharArray()) {
            if (character == Operator.OPENING_PARENTHESIS.getSyntax()) {
                openingCount++;
            } else if (character == Operator.CLOSING_PARENTHESIS.getSyntax()) {
                closingCount++;
            }
        }

        return new int[]{openingCount, closingCount};
    }

    /**
     * Extracts all sub-propositions from this proposition.
     * @return a list of SubProposition objects
     */
    public ArrayList<SubProposition> extractSubProps() {
        ArrayList<SubProposition> result = new ArrayList<>();
        ArrayList<Integer> usedOpeningIndices = new ArrayList<>();

        int closingIndex = -1;

        for (int i = 0; i < parenthesesCount[1]; i++) {
            closingIndex = propString.indexOf(Operator.CLOSING_PARENTHESIS.getSyntax(), closingIndex + 1);

            if (closingIndex == -1) {
                break;
            }

            int openingIndex = findMatchingOpeningParenthesis(closingIndex, usedOpeningIndices);

            if (openingIndex == -1) {
                continue;
            }

            usedOpeningIndices.add(openingIndex);
            SubProposition subProp = createSubProposition(openingIndex, closingIndex, i);
            result.add(subProp);
        }

        return result;
    }

    /**
     * Finds the matching opening parenthesis for a closing parenthesis.
     * @param closingIndex the index of the closing parenthesis
     * @param usedIndices the indices that have already been used
     * @return the index of the matching opening parenthesis, or -1 if not found
     */
    private int findMatchingOpeningParenthesis(int closingIndex, ArrayList<Integer> usedIndices) {
        for (int j = closingIndex; j >= 0; j--) {
            if (propString.charAt(j) == Operator.OPENING_PARENTHESIS.getSyntax()) {
                if (!usedIndices.contains(j)) {
                    return j;
                }
            }
        }
        return -1;
    }

    /**
     * Creates a SubProposition from the indices provided.
     * @param openingIndex the index of the opening parenthesis
     * @param closingIndex the index of the closing parenthesis
     * @param index the index in the parentheses count
     * @return a new SubProposition object
     */
    private SubProposition createSubProposition(int openingIndex, int closingIndex, int index) {
        boolean hasNegation = false;
        int startIndex = openingIndex;

        if (openingIndex > 0 && propString.charAt(openingIndex - 1) == Operator.NEGATION.getSyntax()) {
            hasNegation = true;
            startIndex--;
        }

        String subPropString = propString.substring(startIndex, closingIndex + 1);
        boolean isFullProp = (index == parenthesesCount[1] - 1);

        return new SubProposition(subPropString, mode, hasNegation, propIndex, isFullProp);
    }

    /**
     * Removes all double negations from this proposition.
     * @return the proposition string with double negations removed
     */
    public String removeDoubleNegations() {
        String result = this.getPropString();
        String doubleNegation = Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax();

        while (result.contains(doubleNegation)) {
            result = result.replace(doubleNegation, "");
        }

        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propString, mode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Proposition) {
            Proposition other = (Proposition) obj;
            return propString.equals(other.propString) && mode == other.mode;
        }

        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + mode + " " + propString;
    }

    @Override
    public void setPropString(String propString) {
        super.setPropString(propString);
        initialize();
    }

    /**
     * Gets the parentheses count for this proposition.
     * @return an array [openingCount, closingCount]
     */
    public int[] getParenthesesCount() {
        return parenthesesCount;
    }

    /**
     * Gets the list of sub-propositions.
     * @return the list of SubProposition objects
     */
    public List<SubProposition> getSubProps() {
        return subProps;
    }

    /**
     * Gets the mode of this proposition.
     * @return the Mode (PREMISE or CONCLUSION)
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets the mode of this proposition.
     * @param mode the new Mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }
}

