package argument;

import compoundProposition.AtomicProposition;

public class SubProposition extends AtomicProposition {
    protected Mode mode;
    protected boolean negation;
    protected int propIndex;

    public SubProposition(String propString, Mode mode, boolean negation, int propIndex) {
        this(propString, mode, negation, propIndex, false);
    }

    public SubProposition(String propString, Mode mode, boolean negation, int propIndex, boolean truth) {
        super(propString, truth);
        this.mode = mode;
        this.negation = negation;
        this.propIndex = propIndex;
    }

    public SubProposition(SubProposition subProp){
        this(subProp.getPropString(), subProp.getMode(), subProp.isNegation(), subProp.getPropIndex(), subProp.isTruth());
    }

    public boolean isNegation() {
        return negation;
    }

    public void setNegation(boolean negation) {
        this.negation = negation;
    }

    public int getPropIndex() {
        return propIndex;
    }
    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }
}
