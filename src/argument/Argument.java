package argument;

import compoundProposition.AtomicProposition;
import compoundProposition.Operator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents an argument in the logical system.
 * An argument consists of premises and conclusions, which are represented as logical propositions.
 * It provides methods to validate the argument structure and extract atomic propositions.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class Argument {
    private final Proposition[] props;
    private final Set<AtomicProposition> atomicProps;

    /**
     * Constructs an Argument from premises and conclusions.
     * @param premises an array of premise strings
     * @param conclusions an array of conclusion strings
     */
    public Argument(String[] premises, String[] conclusions) {
        this.props = initializePropositions(premises, conclusions);
        this.atomicProps = extractAtomicProps();
    }

    /**
     * Constructs an Argument from premises and a single conclusion.
     * @param premises an array of premise strings
     * @param conclusion a single conclusion string
     */
    public Argument(String[] premises, String conclusion) {
        this(premises, new String[]{conclusion});
    }

    /**
     * Constructs an Argument from a single premise and conclusions.
     * @param premise a single premise string
     * @param conclusions an array of conclusion strings
     */
    public Argument(String premise, String[] conclusions) {
        this(new String[]{premise}, conclusions);
    }

    /**
     * Constructs an Argument from a single premise and conclusion.
     * @param premise a single premise string
     * @param conclusion a single conclusion string
     */
    public Argument(String premise, String conclusion) {
        this(new String[]{premise}, new String[]{conclusion});
    }

    /**
     * Initializes propositions from premises and conclusions.
     * @param premises an array of premise strings
     * @param conclusions an array of conclusion strings
     * @return an array of Proposition objects
     */
    private Proposition[] initializePropositions(String[] premises, String[] conclusions) {
        Proposition[] allPropositions = new Proposition[premises.length + conclusions.length];
        
        for (int i = 0; i < premises.length; i++) {
            allPropositions[i] = new Proposition(premises[i], Proposition.Mode.PREMISE, i);
        }
        
        for (int i = 0; i < conclusions.length; i++) {
            allPropositions[premises.length + i] = new Proposition(conclusions[i], Proposition.Mode.CONCLUSION, i);
        }
        
        return allPropositions;
    }

    /**
     * Validates that the argument is a well-formed formula.
     * Checks all propositions and their sub-propositions for proper structure.
     * @return true if the argument is well-formed, false otherwise
     */
    public boolean cleanAndCheckIfArgumentIsWellFormedFormula() {
        for (Proposition prop : props) {
            if (!isPropositionWellFormed(prop)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a single proposition is well-formed.
     * @param prop the proposition to check
     * @return true if the proposition is well-formed, false otherwise
     */
    private boolean isPropositionWellFormed(Proposition prop) {
        if (prop.getPropString().isEmpty()) {
            return false;
        }

        int[] parenthesesCount = prop.getParenthesesCount();
        if (parenthesesCount[0] != parenthesesCount[1]) {
            return false;
        }

        return isSubPropositionListWellFormed(prop.getSubProps());
    }

    /**
     * Checks if all sub-propositions in a list are well-formed.
     * @param subProps the list of sub-propositions to check
     * @return true if all sub-propositions are well-formed, false otherwise
     */
    private boolean isSubPropositionListWellFormed(List<SubProposition> subProps) {
        for (int i = 0; i < subProps.size(); i++) {
            if (!isSubPropositionValid(subProps, i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates a single sub-proposition at the given index.
     * @param subProps the list of sub-propositions
     * @param index the index of the sub-proposition to validate
     * @return true if the sub-proposition is valid, false otherwise
     */
    private boolean isSubPropositionValid(List<SubProposition> subProps, int index) {
        String propString = subProps.get(index).getPropString();

        if (!isStructureValid(propString)) {
            return false;
        }

        if (!areOperatorsValid(propString, index, subProps)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if the structure of a sub-proposition string is valid.
     * @param propString the sub-proposition string to check
     * @return true if the structure is valid, false otherwise
     */
    private boolean isStructureValid(String propString) {
        if (propString.isEmpty()) {
            return false;
        }

        if (propString.charAt(propString.length() - 1) != Operator.CLOSING_PARENTHESIS.getSyntax()) {
            return false;
        }

        return isOpeningValid(propString);
    }

    /**
     * Checks if the opening of a sub-proposition is valid.
     * @param propString the sub-proposition string to check
     * @return true if the opening is valid, false otherwise
     */
    private boolean isOpeningValid(String propString) {
        char firstChar = propString.charAt(0);

        if (firstChar == Operator.OPENING_PARENTHESIS.getSyntax()) {
            return true;
        }

        String doubleNegation = Operator.NEGATION.getSyntax() + "" + Operator.OPENING_PARENTHESIS.getSyntax();
        String tripleNegation = Operator.NEGATION.getSyntax() + "" + doubleNegation;

        return propString.startsWith(doubleNegation) || propString.startsWith(tripleNegation);
    }

    /**
     * Checks if all operators in a sub-proposition are valid.
     * @param propString the sub-proposition string to check
     * @param index the index of the sub-proposition in the list
     * @param subProps the list of sub-propositions
     * @return true if all operators are valid, false otherwise
     */
    private boolean areOperatorsValid(String propString, int index, List<SubProposition> subProps) {
        int operatorCount = 0;
        int operatorIndex = -1;

        for (Operator op : Operator.values()) {
            if (op.isParenthesis() || op.isUnary()) {
                continue;
            }

            if (!isOperatorPlacementValid(propString, op)) {
                return false;
            }

            if (propString.contains(op.getSyntax() + "")) {
                operatorCount = countOperatorOccurrences(propString, op.getSyntax());
                if (operatorCount > 1) {
                    return false;
                }
                operatorIndex = propString.indexOf(op.getSyntax());
            }
        }

        return isOperatorContextValid(propString, operatorIndex);
    }

    /**
     * Checks if the placement of an operator within a proposition is valid.
     * @param propString the sub-proposition string
     * @param op the operator to check
     * @return true if the operator placement is valid, false otherwise
     */
    private boolean isOperatorPlacementValid(String propString, Operator op) {
        if (propString.length() < 2 && propString.charAt(propString.length() - 2) == op.getSyntax()) {
            return false;
        }

        if (propString.charAt(1) == op.getSyntax() ||
            propString.charAt(propString.length() - 1) == op.getSyntax() ||
            propString.charAt(0) == op.getSyntax()) {
            return false;
        }

        return true;
    }

    /**
     * Counts the number of occurrences of a character in a string.
     * @param propString the string to search
     * @param character the character to count
     * @return the number of occurrences
     */
    private int countOperatorOccurrences(String propString, char character) {
        int count = 0;
        int index = -1;
        while ((index = propString.indexOf(character, index + 1)) != -1) {
            count++;
        }
        return count;
    }

    /**
     * Checks if the context of an operator (if found) is valid.
     * @param propString the sub-proposition string
     * @param operatorIndex the index of the operator, or -1 if not found
     * @return true if the operator context is valid, false otherwise
     */
    private boolean isOperatorContextValid(String propString, int operatorIndex) {
        if (operatorIndex == -1) {
            return true;
        }

        char beforeOperator = propString.charAt(operatorIndex - 1);
        char afterOperator = propString.charAt(operatorIndex + 1);

        if (beforeOperator == Operator.OPENING_PARENTHESIS.getSyntax() ||
            afterOperator == Operator.CLOSING_PARENTHESIS.getSyntax()) {
            return false;
        }

        return beforeOperator != Operator.NEGATION.getSyntax();
    }

    /**
     * Removes all double negations from the propositions in this argument.
     */
    public void removeDoubleNegations() {
        for (Proposition prop : props) {
            prop.setPropString(prop.removeDoubleNegations());
        }
    }

    /**
     * Extracts all atomic propositions from this argument.
     * @return a LinkedHashSet of all atomic propositions
     */
    public LinkedHashSet<AtomicProposition> extractAtomicProps() {
        LinkedHashSet<AtomicProposition> extracted = new LinkedHashSet<>();

        for (Proposition prop : props) {
            for (SubProposition subProp : prop.getSubProps()) {
                extractAtomicsFromSubProp(subProp.getPropString(), extracted);
            }
        }

        return extracted;
    }

    /**
     * Extracts atomic propositions from a sub-proposition string.
     * @param subPropString the sub-proposition string to parse
     * @param atomicProps the set to add extracted atomic propositions to
     */
    private void extractAtomicsFromSubProp(String subPropString, Set<AtomicProposition> atomicProps) {
        StringBuilder currentAtom = new StringBuilder();

        for (int i = 0; i < subPropString.length(); i++) {
            char character = subPropString.charAt(i);

            if (isOperatorCharacter(character)) {
                if (currentAtom.length() > 0) {
                    atomicProps.add(new AtomicProposition(currentAtom.toString()));
                    currentAtom = new StringBuilder();
                }
            } else {
                currentAtom.append(character);
            }
        }

        if (currentAtom.length() > 0) {
            atomicProps.add(new AtomicProposition(currentAtom.toString()));
        }
    }

    /**
     * Checks if a character is an operator character.
     * @param character the character to check
     * @return true if the character is an operator, false otherwise
     */
    private boolean isOperatorCharacter(char character) {
        for (Operator op : Operator.values()) {
            if (character == op.getSyntax()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all propositions in this argument.
     * @return the array of propositions
     */
    public Proposition[] getProps() {
        return props;
    }

    /**
     * Gets all atomic propositions in this argument.
     * @return the set of atomic propositions
     */
    public Set<AtomicProposition> getAtomicProps() {
        return atomicProps;
    }
}
