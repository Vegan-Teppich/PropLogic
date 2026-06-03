package compoundProposition;

import argument.Proposition;

public class CompoundProposition extends Proposition {
    public static final int opCount = 1;
    public static final int propCount = 2;
    private AtomicProposition[] prop = new AtomicProposition[propCount];
    private Operator op;
    public CompoundProposition(AtomicProposition prop1, Operator op, AtomicProposition prop2, Mode mode, int propIndex){
        super(prop1.getPropString() + op.getSyntax() + prop2.getPropString(), mode, propIndex);
        this.prop[0] = prop1;
        this.op = op;
        this.prop[1] = prop2;
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

    public AtomicProposition[] getProp() {
        return prop;
    }
    public Operator getOperator() {
        return op;
    }

}
