package argument;

import compoundProposition.AtomicProposition;

public class SubProposition extends AtomicProposition {
    protected boolean fullProp;
    protected boolean fullCustomSubProp;

    public SubProposition(String propString, boolean negation, Mode mode,  int propIndex, boolean fullProp, boolean fullCustomSubProp) {
        this(propString, negation, mode, propIndex, fullProp, fullCustomSubProp, false);
    }

    public SubProposition(String propString, boolean negation, Mode mode, int propIndex, boolean fullProp, boolean fullCustomSubProp, boolean truth) {
        super(propString, negation, mode, propIndex, truth);
        this.fullProp = fullProp;
        this.fullCustomSubProp = fullCustomSubProp;
    }

    public SubProposition(SubProposition subProp){
        this(subProp.getPropString(), subProp.isNegation(), subProp.getMode(), subProp.getPropIndex(), subProp.isFullProp(), subProp.isFullCustomSubProp(), subProp.isTruth());
    }

    public boolean isFullProp() {
        return fullProp;
    }

    public boolean isFullCustomSubProp() {
        return fullCustomSubProp;
    }
}
