package compoundProposition;

import java.util.Objects;

public class AtomicProposition {
    protected String propString;
    protected boolean negation;
    protected Mode mode;
    protected int propIndex;
    protected boolean truth;

    public AtomicProposition(String propString, boolean truth){
        this(propString, false, null, -1, truth);
    }
    public AtomicProposition(String propString, boolean negation, Mode mode, int propIndex){
        this(propString, negation, mode, propIndex, false);
    }
    public AtomicProposition(String propString, boolean negation, Mode mode, int propIndex, boolean truth){
        this.propString = propString;
        this.negation = negation;
        this.mode = mode;
        this.propIndex = propIndex;
        this.truth = truth;
    }

    public enum Mode {
        PREMISE,
        CONCLUSION
    }

    @Override
    public int hashCode(){
        return Objects.hash(propString);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj.getClass() == this.getClass()){
            if (((AtomicProposition) obj).propString.equals(this.propString))
                return true;
        }
        return false;
    }

    public boolean isTruth() {
        return truth;
    }

    public void setTruth(boolean truth) {
        this.truth = truth;
    }

    public String getPropString() {
        return propString;
    }

    public void setPropString(String propString) {
        this.propString = propString;
    }

    public boolean isNegation() {
        return negation;
    }

    public Mode getMode() {
        return mode;
    }

    public int getPropIndex() {
        return propIndex;
    }


}
