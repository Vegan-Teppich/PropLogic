package argument;

public class PreviousSubPropIndices {
    String currentSubPropString;


    int atomicBeginIndex = -1;
    int atomicLength;

    int tableIndex = -1;
    TruthTable.TableMode tableMode;


    public PreviousSubPropIndices(String currentSubPropString, int atomicBeginIndex, int atomicLength, int tableIndex, TruthTable.TableMode tableMode){
        this.currentSubPropString = currentSubPropString;
        this.atomicBeginIndex = atomicBeginIndex;
        this.atomicLength = atomicLength;
        this.tableIndex = tableIndex;
        this.tableMode = tableMode;
    }

    public String getCurrentSubPropString() {
        return currentSubPropString;
    }

    public int getAtomicBeginIndex() {
        return atomicBeginIndex;
    }

    public int getAtomicEndIndex(){
        return atomicBeginIndex + atomicLength;
    }

    public void setAtomicBeginIndex(int atomicBeginIndex) {
        this.atomicBeginIndex = atomicBeginIndex;
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

    public int getTableIndex() {
        return tableIndex;
    }

    public void setTableIndex(int tableIndex) {
        this.tableIndex = tableIndex;
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
                tableIndex < 0 ||
                tableMode == null;
    }
}