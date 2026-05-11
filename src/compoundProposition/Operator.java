package compoundProposition;

public enum Operator {
    NEGATION('!'),
    CONJUNCTION('^'),
    DISJUNCTION('v'),
    MATERIAL_IMPLICATION('>'),
    BICONDITIONAL('<'),
    OPENING_PARENTHESIS('('),
    CLOSING_PARENTHESIS(')');

    private final char syntax;
    Operator(char syntax){
        this.syntax = syntax;
    }

    public char getSyntax(){
        return syntax;
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
