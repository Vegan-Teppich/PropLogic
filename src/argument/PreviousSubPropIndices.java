package argument;

public class PreviousSubPropIndices {
    protected String currentSubPropString;


    protected int atomicBeginIndex = -1;
    protected int atomicLength;

    protected int tableXIndex = -1;
    protected TruthTable.TableMode tableMode;


    public PreviousSubPropIndices(){

    }

    public PreviousSubPropIndices(String currentSubPropString, int atomicBeginIndex, int atomicLength, int tableIndex, TruthTable.TableMode tableMode){
        this.currentSubPropString = currentSubPropString;
        this.atomicBeginIndex = atomicBeginIndex;
        this.atomicLength = atomicLength;
        this.tableXIndex = tableIndex;
        this.tableMode = tableMode;
    }

    public String getCurrentSubPropString() {
        return currentSubPropString;
    }
    public void setCurrentSubPropString(String currentSubPropString) {
        this.currentSubPropString = currentSubPropString;
    }

    public int getAtomicBeginIndex() {
        return atomicBeginIndex;
    }
    public void setAtomicBeginIndex(int atomicBeginIndex) {
        this.atomicBeginIndex = atomicBeginIndex;
    }

    public int getAtomicEndIndex(){
        return atomicBeginIndex + atomicLength;
    }
    public void setAtomicEndIndex(int atomicEndIndex){
        atomicLength = atomicEndIndex - atomicBeginIndex;
    }

    public int getAtomicLength() {
        return atomicLength;
    }
    public void setAtomicLength(int atomicLength) {
        this.atomicLength = atomicLength;
    }

    public int getTableXIndex() {
        return tableXIndex;
    }
    public void setTableXIndex(int tableXIndex) {
        this.tableXIndex = tableXIndex;
    }

    public TruthTable.TableMode getTableMode() {
        return tableMode;
    }
    public void setTableMode(TruthTable.TableMode tableMode) {
        this.tableMode = tableMode;
    }

    public boolean checkIfEmpty(){
        return
                currentSubPropString == null ||
                currentSubPropString.isEmpty() ||
                atomicBeginIndex < 0 ||
                atomicLength < 1 ||
                tableXIndex < 0 ||
                tableMode == null;
    }
}