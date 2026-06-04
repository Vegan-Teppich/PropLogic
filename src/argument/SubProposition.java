package argument;

import compoundProposition.AtomicProposition;

public class SubProposition extends AtomicProposition {
    protected Mode mode;
    protected boolean negation;
    protected int propIndex;
    boolean fullProp;

    public SubProposition(String propString, Mode mode, boolean negation, int propIndex, boolean fullProp) {
        this(propString, mode, negation, propIndex, fullProp, false);
    }

    public SubProposition(String propString, Mode mode, boolean negation, int propIndex, boolean fullProp, boolean truth) {
        super(propString, truth);
        this.mode = mode;
        this.negation = negation;
        this.propIndex = propIndex;
        this.fullProp = fullProp;
    }

    public SubProposition(SubProposition subProp){
        this(subProp.getPropString(), subProp.getMode(), subProp.isNegation(), subProp.getPropIndex(), subProp.isFullProp(), subProp.isTruth());
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

    public boolean isFullProp() {
        return fullProp;
    }
}
