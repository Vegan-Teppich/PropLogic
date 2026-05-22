package argument;


public class SubPropAtomicIndices {
    protected String superSubPropString;


    protected int atomicBeginIndex = -1;
    protected int atomicLength;

    protected int tableXIndex = -1;
    protected TruthTable.TableMode tableMode;


    public SubPropAtomicIndices(){

    }

    public SubPropAtomicIndices(String superSubPropString, int atomicBeginIndex, int atomicLength, int tableIndex, TruthTable.TableMode tableMode){
        this.superSubPropString = superSubPropString;
        this.atomicBeginIndex = atomicBeginIndex;
        this.atomicLength = atomicLength;
        this.tableXIndex = tableIndex;
        this.tableMode = tableMode;
    }

    public String getSuperSubPropString() {
        return superSubPropString;
    }
    public void setSuperSubPropString(String superSubPropString) {
        this.superSubPropString = superSubPropString;
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

    public void setEmpty(){
        superSubPropString = null;
        atomicBeginIndex = -1;
        atomicLength = 0;
        tableXIndex = -1;
        tableMode = null;
    }

    public boolean isEmpty(){
        return
                superSubPropString == null ||
                superSubPropString.isEmpty() ||
                atomicBeginIndex < 0 ||
                atomicLength < 1 ||
                tableXIndex < 0 ||
                tableMode == null;
    }
}