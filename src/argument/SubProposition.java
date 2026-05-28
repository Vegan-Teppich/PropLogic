package argument;

import compoundProposition.AtomicProposition;

public class SubProposition extends AtomicProposition {
    protected boolean negation;

    public SubProposition(String propString, Mode mode, boolean negation) {
        super(propString, mode);
        this.negation = negation;
    }

    public SubProposition(String propString, Mode mode, boolean negation, boolean truth) {
        super(propString, mode, truth);
        this.negation = negation;
    }

    public SubProposition(AtomicProposition atomicProp){
        this(atomicProp.getPropString(), atomicProp.getMode(), false, atomicProp.isTruth());
    }

    public SubProposition(SubProposition subProp){
        super(subProp.getPropString(), subProp.getMode(), subProp.isTruth());
        this.negation = subProp.negation;
    }

    public boolean isNegation() {
        return negation;
    }

    public void setNegation(boolean negation) {
        this.negation = negation;
    }
}
