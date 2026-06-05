package compoundProposition;

/**
 * This enum defines all logical operators used in propositional logic expressions.
 * It includes unary operators (negation), binary operators (conjunction, disjunction, implication, biconditional),
 * and parenthesis operators. Each operator has a corresponding syntax character used in the input notation.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public enum Operator {
    NEGATION('!'),
    CONJUNCTION('^'),
    DISJUNCTION('v'),
    MATERIAL_IMPLICATION('>'),
    BICONDITIONAL('<'),
    OPENING_PARENTHESIS('('),
    CLOSING_PARENTHESIS(')');

    private final char syntax;

    /**
     * Constructs an Operator with the given syntax character.
     * @param syntax the character representation of this operator
     */
    Operator(char syntax) {
        this.syntax = syntax;
    }

    /**
     * Gets the syntax character for this operator.
     * @return the character representation of this operator
     */
    public char getSyntax() {
        return syntax;
    }

    /**
     * Gets all unary operators.
     * @return an array containing all unary operators
     */
    public static Operator[] getUnary() {
        return new Operator[]{NEGATION};
    }

    /**
     * Gets all binary operators.
     * @return an array containing all binary operators
     */
    public static Operator[] getBinary() {
        return new Operator[]{CONJUNCTION, DISJUNCTION, MATERIAL_IMPLICATION, BICONDITIONAL};
    }

    /**
     * Gets all parenthesis operators.
     * @return an array containing all parenthesis operators
     */
    public static Operator[] getParentheses() {
        return new Operator[]{OPENING_PARENTHESIS, CLOSING_PARENTHESIS};
    }

    /**
     * Checks if this operator is a unary operator.
     * @return true if this operator is unary, false otherwise
     */
    public boolean isUnary() {
        return this == NEGATION;
    }

    /**
     * Checks if this operator is a binary operator.
     * @return true if this operator is binary, false otherwise
     */
    public boolean isBinary() {
        for (Operator op : getBinary()) {
            if (this == op) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this operator is a parenthesis operator.
     * @return true if this operator is a parenthesis, false otherwise
     */
    public boolean isParenthesis() {
        for (Operator op : getParentheses()) {
            if (this == op) {
                return true;
            }
        }
        return false;
    }
}
