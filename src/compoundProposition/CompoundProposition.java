package compoundProposition;

import argument.Proposition;

/**
 * This class represents a compound proposition, which combines two propositions using a binary logical operator.
 * It extends Proposition and handles the evaluation of compound expressions by computing truth values based on the operator and operands.
 * Compound propositions form the basis of complex logical formulas.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class CompoundProposition extends Proposition {
    public static final int OPERATOR_COUNT = 1;
    public static final int OPERAND_COUNT = 2;

    private final AtomicProposition[] operands;
    private final Operator operator;

    /**
     * Constructs a CompoundProposition from two operands and an operator.
     * @param prop1 the first operand
     * @param operator the binary operator
     * @param prop2 the second operand
     * @param mode the mode (PREMISE or CONCLUSION)
     * @param propIndex the index of this proposition
     */
    public CompoundProposition(AtomicProposition prop1, Operator operator, AtomicProposition prop2, Mode mode, int propIndex) {
        super(buildPropositionString(prop1, operator, prop2), mode, propIndex);
        this.operands = new AtomicProposition[OPERAND_COUNT];
        this.operands[0] = prop1;
        this.operands[1] = prop2;
        this.operator = operator;
        
        boolean truthValue = getCompoundTruthValue(prop1.isTruth(), operator, prop2.isTruth());
        super.setTruth(truthValue);
    }

    /**
     * Calculates the truth value of a compound proposition based on two operands and an operator.
     * @param leftTruth the truth value of the left operand
     * @param operator the binary operator
     * @param rightTruth the truth value of the right operand
     * @return the resulting truth value
     * @throws IllegalArgumentException if the operator is not a binary operator
     */
    public static boolean getCompoundTruthValue(boolean leftTruth, Operator operator, boolean rightTruth) {
        validateOperator(operator);
        
        return switch (operator) {
            case CONJUNCTION -> leftTruth && rightTruth;
            case DISJUNCTION -> leftTruth || rightTruth;
            case MATERIAL_IMPLICATION -> !(leftTruth && !rightTruth);
            case BICONDITIONAL -> leftTruth == rightTruth;
            default -> throw new IllegalArgumentException("Operator " + operator + " is not a binary operator");
        };
    }

    /**
     * Builds the string representation of a compound proposition.
     * @param prop1 the first operand
     * @param operator the operator
     * @param prop2 the second operand
     * @return the compound proposition string
     */
    private static String buildPropositionString(AtomicProposition prop1, Operator operator, AtomicProposition prop2) {
        return prop1.getPropString() + operator.getSyntax() + prop2.getPropString();
    }

    /**
     * Validates that the operator is a binary operator.
     * @param operator the operator to validate
     * @throws IllegalArgumentException if the operator is not binary
     */
    private static void validateOperator(Operator operator) {
        if (operator.isUnary()) {
            throw new IllegalArgumentException("UNARY OPERATOR CANNOT BE USED IN A BINARY CONTEXT: " + operator);
        }
        if (operator.isParenthesis()) {
            throw new IllegalArgumentException("PARENTHESIS OPERATOR CANNOT BE USED IN A BINARY CONTEXT: " + operator);
        }
    }

    /**
     * Gets the first operand of this compound proposition.
     * @return the first operand
     */
    public AtomicProposition getFirstOperand() {
        return operands[0];
    }

    /**
     * Gets the second operand of this compound proposition.
     * @return the second operand
     */
    public AtomicProposition getSecondOperand() {
        return operands[1];
    }

    /**
     * Gets the operator of this compound proposition.
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return String.format("CompoundProposition: %s %c %s", 
            operands[0].getPropString(), 
            operator.getSyntax(), 
            operands[1].getPropString());
    }

    public AtomicProposition[] getProp() {
        return operands;
    }

}
