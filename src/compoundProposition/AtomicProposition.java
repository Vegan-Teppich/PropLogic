package compoundProposition;

import java.util.Objects;

/**
 * This class represents the base proposition unit in the logical system.
 * An atomic proposition is a proposition that cannot be broken down further into simpler propositions.
 * It stores a propositional string and its truth value, serving as the foundation for building complex logical expressions.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class AtomicProposition {
    /**
     * Enum representing the mode of a proposition (PREMISE or CONCLUSION).
     */
    public enum Mode {
        PREMISE,
        CONCLUSION
    }

    protected boolean truth;
    protected String propString;

    /**
     * Constructs an AtomicProposition with the given string representation.
     * @param propString the string representation of this proposition
     */
    public AtomicProposition(String propString) {
        this.propString = propString;
        this.truth = false;
    }

    /**
     * Constructs an AtomicProposition with the given string representation and truth value.
     * @param propString the string representation of this proposition
     * @param truth the truth value of this proposition
     */
    public AtomicProposition(String propString, boolean truth) {
        this.propString = propString;
        this.truth = truth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propString);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof AtomicProposition) {
            AtomicProposition other = (AtomicProposition) obj;
            return propString.equals(other.propString);
        }

        return false;
    }

    @Override
    public String toString() {
        return propString;
    }

    /**
     * Gets the truth value of this proposition.
     * @return true if the proposition is true, false otherwise
     */
    public boolean isTruth() {
        return truth;
    }

    /**
     * Sets the truth value of this proposition.
     * @param truth the new truth value
     */
    public void setTruth(boolean truth) {
        this.truth = truth;
    }

    /**
     * Gets the string representation of this proposition.
     * @return the proposition string
     */
    public String getPropString() {
        return propString;
    }

    /**
     * Sets the string representation of this proposition.
     * @param propString the new proposition string
     */
    public void setPropString(String propString) {
        this.propString = propString;
    }
}

