package argument;

import compoundProposition.AtomicProposition;

/**
 * This class represents a sub-proposition, which is a component of a larger logical proposition.
 * Sub-propositions can be atomic or compound and may have negation applied to them.
 * They are used to break down complex propositions into manageable parts for truth evaluation.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class SubProposition extends AtomicProposition {
    protected Mode mode;
    protected boolean negation;
    protected int propIndex;
    protected boolean fullProp;

    /**
     * Constructs a SubProposition with the given parameters.
     * @param propString the string representation of this sub-proposition
     * @param mode the mode (PREMISE or CONCLUSION)
     * @param negation whether this sub-proposition has a negation
     * @param propIndex the index of the parent proposition
     * @param fullProp whether this is a full proposition
     */
    public SubProposition(String propString, Mode mode, boolean negation, int propIndex, boolean fullProp) {
        this(propString, mode, negation, propIndex, fullProp, false);
    }

    /**
     * Constructs a SubProposition with all parameters including truth value.
     * @param propString the string representation of this sub-proposition
     * @param mode the mode (PREMISE or CONCLUSION)
     * @param negation whether this sub-proposition has a negation
     * @param propIndex the index of the parent proposition
     * @param fullProp whether this is a full proposition
     * @param truth the truth value of this sub-proposition
     */
    public SubProposition(String propString, Mode mode, boolean negation, int propIndex, boolean fullProp, boolean truth) {
        super(propString, truth);
        this.mode = mode;
        this.negation = negation;
        this.propIndex = propIndex;
        this.fullProp = fullProp;
    }

    /**
     * Copy constructor for SubProposition.
     * @param subProp the SubProposition to copy
     */
    public SubProposition(SubProposition subProp) {
        this(subProp.getPropString(), subProp.getMode(), subProp.isNegation(), 
             subProp.getPropIndex(), subProp.isFullProp(), subProp.isTruth());
    }

    /**
     * Checks if this sub-proposition has a negation.
     * @return true if negation is applied, false otherwise
     */
    public boolean isNegation() {
        return negation;
    }

    /**
     * Sets whether this sub-proposition has a negation.
     * @param negation true to apply negation, false otherwise
     */
    public void setNegation(boolean negation) {
        this.negation = negation;
    }

    /**
     * Gets the index of the parent proposition.
     * @return the proposition index
     */
    public int getPropIndex() {
        return propIndex;
    }

    /**
     * Gets the mode of this sub-proposition.
     * @return the Mode (PREMISE or CONCLUSION)
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets the mode of this sub-proposition.
     * @param mode the new Mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Checks if this sub-proposition is a full proposition.
     * @return true if this is a full proposition, false otherwise
     */
    public boolean isFullProp() {
        return fullProp;
    }

    @Override
    public String toString() {
        return String.format("SubProp: %s (negation=%s, mode=%s)", propString, negation, mode);
    }
}
