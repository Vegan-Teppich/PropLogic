package compoundProposition;

import argument.Proposition;

import java.util.Objects;

public class AtomicProposition {
    protected boolean truth;
    protected String propString;

    public AtomicProposition(String propString){
        this.propString = propString;
    }
    public AtomicProposition(String propString, boolean truth){
        this.propString = propString;
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


}
