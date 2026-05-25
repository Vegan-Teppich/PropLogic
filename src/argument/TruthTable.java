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
    List<List<SubProposition>> subPropTable = new ArrayList<>();
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


                // subProps (subProps) code sehr ähnlich deswegen bitte verbessern
                // wenn vergleichbare subProps vorhanden sind
                SubPropAtomicIndices[] atomicsData = new SubPropAtomicIndices[2];
                atomicsData[0] = new SubPropAtomicIndices();
                atomicsData[1] = new SubPropAtomicIndices();
                atomicsData[0].setSuperSubPropString(subPropString);
                atomicsData[1].setSuperSubPropString(subPropString);

                evaluateSubPropsBottomUp(atomicsData);


                searchOperatorsInSubProp(subProp, atomicsData, mode);

            }
        }
    }

    private void searchOperatorsInSubProp(SubProposition subProp, SubPropAtomicIndices[] atomicsData, AtomicProposition.Mode mode) {
        if (atomicsData.length != 2)
            throw new IllegalStateException("CRITICAL ERROR: EXACTLY TWO ATOMIC PLACEHOLDERS MUST BE PASSED.");

        String subPropString = subProp.getPropString();

        SubProposition atomic[] = new SubProposition[atomicsData.length];
        String[] atomicString = new String[atomicsData.length];
        boolean[] atomicNegation = new boolean[atomicsData.length];


        if (atomicsData[0].getAtomicBeginIndex() > -1) {

            if (subPropString.charAt(0) == Operator.NEGATION.getSyntax())
                subProp.setNegation(true);

            // atomicsData.length ist immer 2
            for (int a = 0; a < atomicsData.length; a++) {

                if (atomicsData[a].getAtomicBeginIndex() > 0)
                    if (subPropString.charAt(atomicsData[a].getAtomicBeginIndex() - 1) == Operator.NEGATION.getSyntax())
                        atomicNegation[a] = true;

                atomicString[a] = subPropString.substring(atomicsData[a].getAtomicBeginIndex(), atomicsData[a].getAtomicEndIndex());
                atomic[a] = new SubProposition(atomicString[a], mode, atomicNegation[a]);

                if (a == 1) {
                    if (atomicsData[a].getAtomicBeginIndex() <= -1) {
                        fillInColumn(subProp, atomic[0], atomicsData[0]);
                        break;
                    }

                    Operator op = null;
                    String opSubstring = subPropString.substring(atomicsData[0].getAtomicEndIndex(), atomicsData[1].getAtomicBeginIndex());
                    for (Operator thisOp : Operator.getBinary()) {
                        if (opSubstring.contains(thisOp.getSyntax() + "")) {
                            op = thisOp;
                            break;
                        }
                    }
                    if (op == null)
                        throw new IllegalStateException("NO OPERATOR FOUND");
                    fillInColumn(subProp, atomicsData,   atomic[0], op, atomic[1]);
                }

            }

        } else {
            throw new IllegalStateException("CHECK WHY THERE WAS NO FIRST ATOMIC_PROP FOUND IN SUB_PROP");
        }
    }

    private void evaluateSubPropsBottomUp(SubPropAtomicIndices[] atomicsData) {

        if (atomicsData.length != 2)
            throw new IllegalStateException("CRITICAL ERROR: EXACTLY TWO ATOMIC PLACEHOLDERS MUST BE PASSED.");

        String subPropString = atomicsData[0].getSuperSubPropString();

        for (TableMode tableMode : TableMode.values()) {

            if (tableMode == TableMode.SUB) {
                if (subPropTable.get(0).size() < 1) {
                    continue;
                }
            }

            if (atomicsData[1].getAtomicBeginIndex() > -1 && atomicsData[0].getAtomicBeginIndex() > -1)
                continue;

            if (tableMode == TableMode.SUB) {
                for (int x = subPropTable.get(0).size() - 1; x >= 0; x--) {
                    searchAndSaveAtomicData(subPropString, atomicsData, tableMode, x);
                    if (atomicsData[0].getAtomicBeginIndex() > -1 && atomicsData[1].getAtomicBeginIndex() > -1) {
                        break;
                    }
                }
            } else if (tableMode == TableMode.ATOMIC) {
                for (int x = 0; x < atomicPropTable.length; x++) {
                    searchAndSaveAtomicData(subPropString, atomicsData, tableMode, x);
                    if (atomicsData[0].getAtomicBeginIndex() > -1 && atomicsData[1].getAtomicBeginIndex() > -1) {
                        break;
                    }
                }
            }


        }


    }

    private void searchAndSaveAtomicData(String subPropString, SubPropAtomicIndices[] atomicsData, TableMode tableMode, int tableXIndex) {
        String previousSubProp = null;
        if (tableMode == TableMode.SUB) {
            previousSubProp = subPropTable.get(0).get(tableXIndex).getPropString();
        } else if (tableMode == TableMode.ATOMIC) {
            previousSubProp = atomicPropTable[tableXIndex][0].getPropString();
        }
        // atomicsData.length ist immer 2
        for (int a = 0; a < atomicsData.length; a++) {
            if (atomicsData[a].getAtomicBeginIndex() <= -1) {
                if (a == 0) {
                    atomicsData[a].setAtomicBeginIndex(subPropString.indexOf(previousSubProp));
                } else if (a == 1) {
                    atomicsData[a].setAtomicBeginIndex(subPropString.lastIndexOf(previousSubProp));
                }

                if (atomicsData[a].getAtomicBeginIndex() > -1) {
                    atomicsData[a].setAtomicLength(previousSubProp.length());
                    atomicsData[a].setTableMode(TableMode.SUB);
                    atomicsData[a].setTableXIndex(tableXIndex);

                    if (a == 1)
                        convAtomicsRightPos(atomicsData);
                } else {
                    break;
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
            subPropTable.add(new ArrayList<>());
        }
    }


    private SecondAtomicPositionValidity isAtomic2Valid(SubPropAtomicIndices[] previousAtomics) {
        if (
                (previousAtomics[0].getAtomicBeginIndex() >= previousAtomics[1].getAtomicBeginIndex() && previousAtomics[0].getAtomicBeginIndex() < previousAtomics[1].getAtomicEndIndex()) ||
                        (previousAtomics[0].getAtomicEndIndex() > previousAtomics[1].getAtomicBeginIndex() && previousAtomics[0].getAtomicEndIndex() <= previousAtomics[1].getAtomicEndIndex()) ||
                        (previousAtomics[1].getAtomicBeginIndex() >= previousAtomics[0].getAtomicBeginIndex() && previousAtomics[1].getAtomicBeginIndex() < previousAtomics[0].getAtomicEndIndex()) ||
                        (previousAtomics[1].getAtomicEndIndex() > previousAtomics[0].getAtomicBeginIndex() && previousAtomics[1].getAtomicEndIndex() <= previousAtomics[0].getAtomicEndIndex())
        ) {
            return SecondAtomicPositionValidity.INVALID;

        } else if (previousAtomics[0].getAtomicBeginIndex() < previousAtomics[1].getAtomicBeginIndex()) {
            return SecondAtomicPositionValidity.VALID;

        } else if (previousAtomics[1].getAtomicBeginIndex() <= previousAtomics[0].getAtomicBeginIndex()) {
            return SecondAtomicPositionValidity.FLIP_VALID;
        }

        return SecondAtomicPositionValidity.INVALID;
    }


    private void fillInColumn(
            SubProposition subProp,
            SubPropAtomicIndices[] atomicsData,

            SubProposition atomic1,
            Operator op,
            SubProposition atomic2
    ) {
        if (atomicsData.length != 2)
            throw new IllegalStateException("CRITICAL ERROR: EXACTLY TWO ATOMIC PLACEHOLDERS MUST BE PASSED.");

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

            AtomicProposition[] atomicsFromTableIndices = new AtomicProposition[atomicsData.length];
            String[] newAtomicStrings = new String[atomicsData.length];
            boolean atomicsTruth[] = new boolean[atomicsData.length];

            // exakt 2 durchläufe
            for (int a = 0; a < atomicsData.length; a++){
                if (atomicsData[a].getTableMode() == TableMode.ATOMIC) {
                    atomicsFromTableIndices[a] = atomicPropTable[atomicsData[0].getTableXIndex()][y];
                } else if (atomicsData[a].getTableMode() == TableMode.SUB) {
                    atomicsFromTableIndices[a] = subPropTable.get(y).get(atomicsData[0].getTableXIndex());
                }
                newAtomicStrings[a] = atomicsFromTableIndices[a].getPropString();
                atomicsTruth[a] = atomicsFromTableIndices[a].isTruth();

            }

            if (atomic1.isNegation())
                atomicsTruth[0] = !atomicsTruth[0];
            if (atomic2.isNegation())
                atomicsTruth[1] = !atomicsTruth[1];

            if (atomic1.getPropString().equals(newAtomicStrings[0]) && atomic2.getPropString().equals(newAtomicStrings[1])) {
                boolean cpTruth = CompoundProposition.getCompoundTruthValue(atomicsTruth[0], op, atomicsTruth[1]);
                if (subProp.isNegation()) {
                    cpTruth = !cpTruth;
                }
                subProp.setTruth(cpTruth);
                subPropTable.get(y).add(subProp);
            } else {
                throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROPS DO NOT MATCH. THE CURRENT COMPOUND_PROP MUST BE DERIVED FROM A PAIR OF THE TABLES PREVIOUS PROPS.");
            }

        }

    }

    private void fillInColumn(
            SubProposition subProp,

            SubProposition atomic1,
            SubPropAtomicIndices atomic1Data
    ) {
        for (int y = 0; y < rowCount; y++) {

            AtomicProposition atomic1FromTableIndex = null;
            String newAtomic1String;

            if (atomic1Data.getTableMode() == TableMode.ATOMIC) {
                atomic1FromTableIndex = atomicPropTable[atomic1Data.getTableXIndex()][y];
            } else if (atomic1Data.getTableMode() == TableMode.SUB) {
                atomic1FromTableIndex = subPropTable.get(y).get(atomic1Data.getTableXIndex());
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
                subPropTable.get(y).add(subProp);
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
        for (int x = 0; x < atomicPropTable.length + subPropTable.get(0).size(); x++) {
            if (x == 0)
                System.out.print(" | ");

            String propString;
            if (x < atomicPropTable.length) {
                propString = atomicPropTable[x][0].getPropString();
            } else {
                propString = subPropTable.get(0).get(x - atomicPropTable.length).getPropString();
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
            for (int x = 0; x < atomicPropTable.length + subPropTable.get(0).size(); x++) {
                if (x == 0)
                    System.out.print(" | ");

                if (x < atomicPropTable.length) {
                    if (atomicPropTable[x][y].isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);
                } else {
                    if (subPropTable.get(y).get(x - atomicPropTable.length).isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);

                }

            }
            System.out.println();
        }
    }
}
