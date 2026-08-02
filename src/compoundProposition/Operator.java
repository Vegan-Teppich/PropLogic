package compoundProposition;

public enum Operator {
    OPENING_PARENTHESIS('(', 0),
    CLOSING_PARENTHESIS(')', 0),
    NEGATION('!', 1),
    CONJUNCTION('^', 2),
    DISJUNCTION('v', 3),
    MATERIAL_IMPLICATION('>', 4),
    BICONDITIONAL('<', 5);


    private final char syntax;
    private final int priority;
    Operator(char syntax, int priority){
        this.syntax = syntax;
        this.priority = priority;
    }

    public char getSyntax(){
        return syntax;
    }
    public int getPriority() {
        return priority;
    }
    public static Operator[] getUnary(){
        return new Operator[]{NEGATION};
    }
    public static Operator[] getBinary(){
        return new Operator[]{CONJUNCTION, DISJUNCTION, MATERIAL_IMPLICATION, BICONDITIONAL};
    }

    public static Operator[] getParentheses(){
        return new Operator[]{OPENING_PARENTHESIS, CLOSING_PARENTHESIS};
    }
}
