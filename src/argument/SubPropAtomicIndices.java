package argument;

/**
 * This class stores metadata about atomic propositions within sub-propositions.
 * It tracks the position and length of atomic propositions within their parent sub-proposition string
 * and their location in the truth table. This information is used during truth table generation and evaluation.
 * @author: TeppichKnecht
 * @version: 1.0.1
 * @since: 2024-06
 */
public class SubPropAtomicIndices {
    private static final int UNINITIALIZED = -1;
    private static final int ZERO_LENGTH = 0;

    private String superSubPropString;
    int atomicBeginIndex;
    private int atomicLength;
    private int tableXIndex;
    private TruthTable.TableMode tableMode;

    /**
     * Constructs an empty SubPropAtomicIndices.
     */
    public SubPropAtomicIndices() {
        this.atomicBeginIndex = UNINITIALIZED;
        this.atomicLength = ZERO_LENGTH;
        this.tableXIndex = UNINITIALIZED;
    }

    /**
     * Constructs a SubPropAtomicIndices with all values initialized.
     * @param superSubPropString the parent sub-proposition string
     * @param atomicBeginIndex the begin index of the atomic proposition
     * @param atomicLength the length of the atomic proposition
     * @param tableIndex the index in the truth table
     * @param tableMode the table mode (ATOMIC or SUB)
     */
    public SubPropAtomicIndices(String superSubPropString, int atomicBeginIndex, int atomicLength, 
                                int tableIndex, TruthTable.TableMode tableMode) {
        this.superSubPropString = superSubPropString;
        this.atomicBeginIndex = atomicBeginIndex;
        this.atomicLength = atomicLength;
        this.tableXIndex = tableIndex;
        this.tableMode = tableMode;
    }

    /**
     * Gets the parent sub-proposition string.
     * @return the super sub-proposition string
     */
    public String getSuperSubPropString() {
        return superSubPropString;
    }

    /**
     * Sets the parent sub-proposition string.
     * @param superSubPropString the super sub-proposition string
     */
    public void setSuperSubPropString(String superSubPropString) {
        this.superSubPropString = superSubPropString;
    }

    /**
     * Gets the begin index of the atomic proposition.
     * @return the atomic begin index
     */
    public int getAtomicBeginIndex() {
        return atomicBeginIndex;
    }

    /**
     * Sets the begin index of the atomic proposition.
     * @param atomicBeginIndex the atomic begin index
     */
    public void setAtomicBeginIndex(int atomicBeginIndex) {
        this.atomicBeginIndex = atomicBeginIndex;
    }

    /**
     * Gets the end index of the atomic proposition.
     * @return the atomic end index (begin + length)
     */
    public int getAtomicEndIndex() {
        return atomicBeginIndex + atomicLength;
    }

    /**
     * Sets the end index of the atomic proposition.
     * @param atomicEndIndex the atomic end index
     */
    public void setAtomicEndIndex(int atomicEndIndex) {
        this.atomicLength = atomicEndIndex - atomicBeginIndex;
    }

    /**
     * Gets the length of the atomic proposition.
     * @return the atomic length
     */
    public int getAtomicLength() {
        return atomicLength;
    }

    /**
     * Sets the length of the atomic proposition.
     * @param atomicLength the atomic length
     */
    public void setAtomicLength(int atomicLength) {
        this.atomicLength = atomicLength;
    }

    /**
     * Gets the table X index.
     * @return the table X index
     */
    public int getTableXIndex() {
        return tableXIndex;
    }

    /**
     * Sets the table X index.
     * @param tableXIndex the table X index
     */
    public void setTableXIndex(int tableXIndex) {
        this.tableXIndex = tableXIndex;
    }

    /**
     * Gets the table mode.
     * @return the table mode
     */
    public TruthTable.TableMode getTableMode() {
        return tableMode;
    }

    /**
     * Sets the table mode.
     * @param tableMode the table mode
     */
    public void setTableMode(TruthTable.TableMode tableMode) {
        this.tableMode = tableMode;
    }

    /**
     * Clears all indices and resets to empty state.
     */
    public void setEmpty() {
        this.superSubPropString = null;
        this.atomicBeginIndex = UNINITIALIZED;
        this.atomicLength = ZERO_LENGTH;
        this.tableXIndex = UNINITIALIZED;
        this.tableMode = null;
    }

    /**
     * Checks if this SubPropAtomicIndices is empty or uninitialized.
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return superSubPropString == null ||
               superSubPropString.isEmpty() ||
               atomicBeginIndex < 0 ||
               atomicLength < 1 ||
               tableXIndex < 0 ||
               tableMode == null;
    }

    @Override
    public String toString() {
        return String.format("SubPropAtomicIndices[begin=%d, length=%d, tableXIndex=%d, mode=%s]",
            atomicBeginIndex, atomicLength, tableXIndex, tableMode);
    }
}