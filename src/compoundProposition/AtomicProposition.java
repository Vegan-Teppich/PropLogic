package compoundProposition;

import argument.Proposition;

import java.util.Objects;

public class AtomicProposition {
    protected boolean truth;
    protected Proposition.Mode mode;
    protected String propString;

    public AtomicProposition(String propString, Mode mode){
        this.propString = propString;
        this.mode = mode;
    }
    public AtomicProposition(String propString, Mode mode, boolean truth){
        this.propString = propString;
        this.mode = mode;
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

    public Proposition.Mode getMode() {
        return mode;
    }

    public void setMode(Proposition.Mode mode) {
        this.mode = mode;
    }

}
