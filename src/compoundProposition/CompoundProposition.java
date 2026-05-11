package compoundProposition;

import argument.Proposition;

public class CompoundProposition extends Proposition {
    private AtomicProposition prop1;
    private Operator op;
    private AtomicProposition prop2;
    public CompoundProposition(AtomicProposition prop1, Operator op, AtomicProposition prop2, Mode mode){
        super(prop1.getPropString() + op.getSyntax() + prop2.getPropString(), mode);
        this.prop1 = prop1;
        this.op = op;
        this.prop2 = prop2;
        super.setTruth(getCompoundTruthValue(prop1.isTruth(), op, prop2.isTruth()));

    }

    public static boolean getCompoundTruthValue(boolean prop1Truth, Operator op, boolean prop2Truth) {

        IllegalArgumentException unaryOpEx = new IllegalArgumentException("UNARY OPERATOR CAN NOT BE USED IN A BINARY CONTEXT!");
        IllegalArgumentException parenthesisOpEx = new IllegalArgumentException("PARENTHESIS OPERATOR CAN NOT BE USED IN A BINARY CONTEXT!");
        IllegalArgumentException otherOpEx = new IllegalArgumentException("UNKNOWN OPERATOR!");

        for (Operator thisOp : Operator.getUnary()){
            if (op == thisOp)
                throw unaryOpEx;
        }
        for (Operator thisOp : Operator.getParentheses()){
            if (op == thisOp)
                throw parenthesisOpEx;
        }

        switch (op){

            case CONJUNCTION:
                return prop1Truth && prop2Truth;


            case DISJUNCTION:
                return prop1Truth || prop2Truth;


            case MATERIAL_IMPLICATION:
                return  !(prop1Truth && !prop2Truth);


            case BICONDITIONAL:
                return  (prop1Truth && prop2Truth) || (!prop1Truth && !prop2Truth);

        }
        throw otherOpEx;
    }

    public AtomicProposition getProp1() {
        return prop1;
    }
    public Operator getOperator() {
        return op;
    }
    public AtomicProposition getProp2() {
        return prop2;
    }
}
