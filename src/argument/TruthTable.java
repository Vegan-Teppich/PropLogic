package argument;

import compoundProposition.CompoundProposition;
import compoundProposition.Operator;
import compoundProposition.AtomicProposition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TruthTable {

    Argument arg;
    AtomicProposition[][] atomicPropTable;
    List<List<SubProposition>> propTable = new ArrayList<>();
    int rowCount;

    public TruthTable(Argument arg) {
        this.arg = arg;
        fillInTable();
    }

    private void fillInTable() {

        Proposition[] props = arg.getProps();

        Set<AtomicProposition> atomicProps = arg.getAtomicProps();

        calculateRowsAndDefineSpaceInTables(atomicProps.size());

        atomicTableTruthAssignmentEnumeration(atomicProps);


        // subProps
        for (int p = 0; p < props.length; p++) {

            Proposition prop = props[p];
            List<SubProposition> subProps = prop.getSubProps();
            AtomicProposition.Mode mode = prop.getMode();

            for (int x = 0; x < subProps.size(); x++) {
                SubProposition subProp = subProps.get(x);
                String subPropString = subProp.getPropString();


                TableMode atomic1TableMode = null;
                int atomic1TableIndex = -1;
                int[] atomic1SubPropIndices = {-1, -1};

                String atomic1String;
                boolean atomic1Negation = false;


                TableMode atomic2TableMode = null;
                int atomic2TableIndex = -1;
                int[] atomic2SubPropIndices = {-1, -1};

                String atomic2String;
                boolean atomic2Negation = false;


                // subProps (subProps) code sehr ähnlich deswegen bitte verbessern
                // wenn vergleichbare subProps vorhanden sind
                SubPropAtomicIndices[] previousAtomics = new SubPropAtomicIndices[2];
                previousAtomics[0] = new SubPropAtomicIndices();
                previousAtomics[1] = new SubPropAtomicIndices();
                previousAtomics[0].setCurrentSubPropString(subPropString);
                previousAtomics[1].setCurrentSubPropString(subPropString);

                evaluateSubPropsBottomUp(previousAtomics);


                // subProps (atomicProps) code sehr ähnlich deswegen bitte verbessern


                if (atomic1SubPropIndices[0] != -1 && atomic1SubPropIndices[1] != -1) {

                    if (subPropString.charAt(0) == Operator.NEGATION.getSyntax())
                        subProp.setNegation(true);

                    if (atomic1SubPropIndices[0] > 0)
                        if (subPropString.charAt(atomic1SubPropIndices[0] - 1) == Operator.NEGATION.getSyntax())
                            atomic1Negation = true;

                    atomic1String = subPropString.substring(atomic1SubPropIndices[0], atomic1SubPropIndices[1]);
                    SubProposition atomic1 = new SubProposition(atomic1String, mode, atomic1Negation);

                    if (atomic2SubPropIndices[0] != -1 && atomic2SubPropIndices[1] != -1) {

                        if (atomic2SubPropIndices[0] > 0)
                            if (subPropString.charAt(atomic2SubPropIndices[0] - 1) == Operator.NEGATION.getSyntax())
                                atomic2Negation = true;

                        atomic2String = subPropString.substring(atomic2SubPropIndices[0], atomic2SubPropIndices[1]);
                        SubProposition atomic2 = new SubProposition(atomic2String, mode, atomic2Negation);


                        Operator op = null;
                        String opSubstring = subPropString.substring(atomic1SubPropIndices[1], atomic2SubPropIndices[0]);
                        for (Operator thisOp : Operator.getBinary()) {
                            if (opSubstring.contains(thisOp.getSyntax() + "")) {
                                op = thisOp;
                                break;
                            }
                        }
                        if (op == null)
                            throw new IllegalStateException("NO OPERATOR FOUND");
                        fillInColumn(subProp, atomic1, atomic1TableMode, atomic1TableIndex, op, atomic2, atomic2TableMode, atomic2TableIndex);
                    } else {
                        fillInColumn(subProp, atomic1, atomic1TableMode, atomic1TableIndex);
                    }
                } else {
                    throw new IllegalStateException("CHECK WHY THERE WAS NO FIRST ATOMIC_PROP FOUND IN SUB_PROP");
                }


            }
        }
    }

    private void evaluateSubPropsBottomUp(SubPropAtomicIndices[] previousAtomics) {

        if (previousAtomics.length != 2)
            throw new IllegalStateException("CRITICAL ERROR: EXACTLY TWO ATOMIC PLACEHOLDERS MUST BE PASSED.");

        for (TableMode tm : TableMode.values()) {

            if (tm == TableMode.SUB)
                if (propTable.get(0).size() <= 0) {
                    continue;
                }

            String subPropString = previousAtomics[0].getCurrentSubPropString();

            for (int x = propTable.get(0).size() - 1; x >= 0; x--) {
                String previousSubProp = propTable.get(0).get(x).getPropString();
                // previousAtomics.length ist immer 2
                for (int a = 0; a < previousAtomics.length; a++) {
                    if (previousAtomics[a].getAtomicBeginIndex() <= -1) {
                        if (a == 0) {
                            previousAtomics[a].setAtomicBeginIndex(subPropString.indexOf(previousSubProp));
                        } else if (a == 1) {
                            previousAtomics[a].setAtomicBeginIndex(subPropString.lastIndexOf(previousSubProp));
                        }

                        if (previousAtomics[a].getAtomicBeginIndex() > -1) {
                            previousAtomics[a].setAtomicLength(previousSubProp.length());
                            previousAtomics[a].setTableMode(TableMode.SUB);
                            previousAtomics[a].setTableXIndex(x);

                            if (a == 1)
                                convAtomicsRightPos(previousAtomics);
                        } else {
                            break;
                        }
                    }
                }
                if (previousAtomics[0].getAtomicBeginIndex() > -1 && previousAtomics[1].getAtomicBeginIndex() > -1) {
                    break;
                }

            }

        }


        if (previousAtomics[1].getAtomicBeginIndex() <= -1) {
            for (int i = 0; i < atomicPropTable.length; i++) {
                if (previousAtomics[0].getAtomicBeginIndex() <= -1) {

                    previousAtomics[0].setAtomicBeginIndex(subPropString.indexOf(atomicPropTable[i][0].getPropString()));

                    // kann man auch am ende checken
                    if (previousAtomics[0].getAtomicBeginIndex() > -1) {
                        previousAtomics[0].setAtomicLength(atomicPropTable[i][0].getPropString().length());
                        ;
                        previousAtomics[0].setTableMode(TableMode.ATOMIC);
                        previousAtomics[0].setTableXIndex(i);
                    }
                    continue;
                }

                previousAtomics[1].setAtomicBeginIndex(subPropString.lastIndexOf(atomicPropTable[i][0].getPropString()));

                if (previousAtomics[1].getAtomicBeginIndex() > -1) {
                    previousAtomics[1].setAtomicLength(atomicPropTable[i][0].getPropString().length());
                    previousAtomics[1].setTableMode(TableMode.ATOMIC);
                    previousAtomics[1].setTableXIndex(i);

                    convAtomicsRightPos(previousAtomics);
                }

            }
        }
    }

    private void convAtomicsRightPos(SubPropAtomicIndices[] previousAtomics) {
        SecondAtomicPositionValidity atomic2IndexValid = isAtomic2Valid(previousAtomics);

        if (atomic2IndexValid == SecondAtomicPositionValidity.INVALID) {
            previousAtomics[1].setEmpty();
        } else if (atomic2IndexValid == SecondAtomicPositionValidity.FLIP_VALID) {
            SubPropAtomicIndices flipAtomic = previousAtomics[0];
            previousAtomics[0] = previousAtomics[1];
            previousAtomics[1] = flipAtomic;
        } else if (atomic2IndexValid == SecondAtomicPositionValidity.VALID) {

        }
    }

    private void atomicTableTruthAssignmentEnumeration(Collection<AtomicProposition> atomicProps) {
        int adjacentTruthRows = rowCount;
        int x = -1;
        for (AtomicProposition atomicProp : atomicProps) {
            x++;
            adjacentTruthRows = adjacentTruthRows / 2;
            boolean truth = false;
            int y = -1;
            for (int y1 = 0; y1 < rowCount / adjacentTruthRows; y1++) {
                truth = !truth;
                for (int y2 = 0; y2 < adjacentTruthRows; y2++) {
                    y++;
                    atomicPropTable[x][y] = new AtomicProposition(atomicProp.getPropString(), atomicProp.getMode(), truth);
                }
            }
        }
    }

    private void calculateRowsAndDefineSpaceInTables(int atomicPropCount) {
        rowCount = 1;
        for (int x = 0; x < atomicPropCount; x++) {
            rowCount = rowCount * 2;
        }
        atomicPropTable = new AtomicProposition[atomicPropCount][rowCount];

        for (int y = 0; y < rowCount; y++) {
            propTable.add(new ArrayList<>());
        }
    }


    private SecondAtomicPositionValidity isAtomic2Valid(SubPropAtomicIndices[] previousAtomics) {
        if (
                (previousAtomics[0].getAtomicBeginIndex() >= previousAtomics[1].getAtomicBeginIndex() && previousAtomics[0].getAtomicBeginIndex() < previousAtomics[1].getAtomicEndIndex()) ||
                        (previousAtomics[0].getAtomicEndIndex() > previousAtomics[1].getAtomicBeginIndex() && previousAtomics[0].getAtomicEndIndex() <= previousAtomics[1].getAtomicEndIndex()) ||
                        (previousAtomics[1].getAtomicBeginIndex() >= previousAtomics[0].getAtomicBeginIndex() && previousAtomics[1].getAtomicBeginIndex() < previousAtomics[0].getAtomicEndIndex()) ||
                        (previousAtomics[1].getAtomicEndIndex() > previousAtomics[0].getAtomicBeginIndex() && previousAtomics[1].getAtomicEndIndex() <= previousAtomics[0].getAtomicEndIndex())
        )
            return SecondAtomicPositionValidity.INVALID;
        if (previousAtomics[0].getAtomicBeginIndex() < previousAtomics[1].getAtomicBeginIndex()) {
            return SecondAtomicPositionValidity.VALID;
        } else if (previousAtomics[1].getAtomicBeginIndex() <= previousAtomics[0].getAtomicBeginIndex()) {
            return SecondAtomicPositionValidity.FLIP_VALID;
        }

        return SecondAtomicPositionValidity.INVALID;
    }


    private void fillInColumn(
            SubProposition subProp,

            SubProposition atomic1,
            TableMode atomic1TableMode,
            int atomic1TableXIndex,

            Operator op,

            SubProposition atomic2,
            TableMode atomic2TableMode,
            int atomic2TableXIndex
    ) {

        boolean noOp = false;
        for (Operator thisOp : Operator.getParentheses()) {
            if (op == thisOp) {
                noOp = true;
            }
        }

        for (Operator thisOp : Operator.getUnary()) {
            if (op == thisOp) {
                noOp = true;
            }
        }

        if (noOp)
            throw new IllegalStateException("WRONG OPERATOR");

        for (int y = 0; y < rowCount; y++) {

            AtomicProposition atomic1FromTableIndex = null;
            AtomicProposition atomic2FromTableIndex = null;

            String newAtomic1String;
            String newAtomic2String;

            if (atomic1TableMode == TableMode.ATOMIC) {
                atomic1FromTableIndex = atomicPropTable[atomic1TableXIndex][y];
            } else if (atomic1TableMode == TableMode.SUB) {
                atomic1FromTableIndex = propTable.get(y).get(atomic1TableXIndex);
            }
            if (atomic2TableMode == TableMode.ATOMIC) {
                atomic2FromTableIndex = atomicPropTable[atomic2TableXIndex][y];
            } else if (atomic2TableMode == TableMode.SUB) {
                atomic2FromTableIndex = propTable.get(y).get(atomic2TableXIndex);
            }
            newAtomic1String = atomic1FromTableIndex.getPropString();
            newAtomic2String = atomic2FromTableIndex.getPropString();

            boolean atomic1Truth = atomic1FromTableIndex.isTruth();
            boolean atomic2Truth = atomic2FromTableIndex.isTruth();
            if (atomic1.isNegation())
                atomic1Truth = !atomic1Truth;
            if (atomic2.isNegation())
                atomic2Truth = !atomic2Truth;

            if (atomic1.getPropString().equals(newAtomic1String) && atomic2.getPropString().equals(newAtomic2String)) {
                boolean cpTruth = CompoundProposition.getCompoundTruthValue(atomic1Truth, op, atomic2Truth);
                if (subProp.isNegation()) {
                    cpTruth = !cpTruth;
                }
                subProp.setTruth(cpTruth);
                propTable.get(y).add(subProp);
            } else {
                throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROPS DO NOT MATCH. THE CURRENT COMPOUND_PROP MUST BE DERIVED FROM A PAIR OF THE TABLES PREVIOUS PROPS.");
            }

        }

    }

    private void fillInColumn(
            SubProposition subProp,

            SubProposition atomic1,
            TableMode atomic1TableMode,
            int atomic1TableXIndex
    ) {
        for (int y = 0; y < rowCount; y++) {

            AtomicProposition atomic1FromTableIndex = null;
            String newAtomic1String;

            if (atomic1TableMode == TableMode.ATOMIC) {
                atomic1FromTableIndex = atomicPropTable[atomic1TableXIndex][y];
            } else if (atomic1TableMode == TableMode.SUB) {
                atomic1FromTableIndex = propTable.get(y).get(atomic1TableXIndex);
            }

            newAtomic1String = atomic1FromTableIndex.getPropString();
            boolean atomic1Truth = atomic1FromTableIndex.isTruth();
            if (atomic1.isNegation())
                atomic1Truth = !atomic1Truth;

            if (atomic1.getPropString().equals(newAtomic1String)) {
                if (subProp.isNegation()) {
                    subProp.setTruth(!atomic1Truth);
                } else {
                    subProp.setTruth(atomic1Truth);
                }
                propTable.get(y).add(subProp);
            } else {
                throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROP DOES NOT MATCH. THE CURRENT PROP MUST BE DERIVED FROM TABLES PREVIOUS PROPS.");
            }

        }
    }

    public enum TableMode {
        ATOMIC,
        SUB
    }

    private enum SecondAtomicPositionValidity {
        VALID,
        INVALID,
        FLIP_VALID
    }

    public void print() {
        for (int x = 0; x < atomicPropTable.length + propTable.get(0).size(); x++) {
            if (x == 0)
                System.out.print(" | ");

            String propString;
            if (x < atomicPropTable.length) {
                propString = atomicPropTable[x][0].getPropString();
            } else {
                propString = propTable.get(0).get(x - atomicPropTable.length).getPropString();
            }
            System.out.print(propString);
            for (int i = 0; i < 5 - propString.length(); i++) {
                System.out.print(" ");
            }
            System.out.print(" | ");
            //  + "     | "
        }


        System.out.println();


        String tablePosPrintTrue = true + "  | ";
        String tablePosPrintFalse = false + " | ";
        for (int y = 0; y < rowCount; y++) {
            for (int x = 0; x < atomicPropTable.length + propTable.get(0).size(); x++) {
                if (x == 0)
                    System.out.print(" | ");

                if (x < atomicPropTable.length) {
                    if (atomicPropTable[x][y].isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);
                } else {
                    if (propTable.get(y).get(x - atomicPropTable.length).isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);

                }

            }
            System.out.println();
        }
    }
}
