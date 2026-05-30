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

    IllegalStateException noAtomicFoundEx = new IllegalStateException("CHECK WHY THERE WAS LESS THAN ONE ATOMIC_PROPS FOUND IN PREVIOUS SUB_PROP");

    public TruthTable(Argument arg) {
        this.arg = arg;
        fillInTable();
    }

    private void fillInTable() {

        Proposition[] props = arg.getProps();

        Set<AtomicProposition> atomicProps = arg.getAtomicProps();

        calculateRowsAndDefineSpaceInTables(atomicProps.size());

        atomicTableTruthAssignmentEnumeration(atomicProps);


        for (int p = 0; p < props.length; p++) {

            Proposition prop = props[p];
            List<SubProposition> subProps = prop.getSubProps();
            AtomicProposition.Mode mode = prop.getMode();

            for (int x = 0; x < subProps.size(); x++) {
                SubProposition subProp = subProps.get(x);
                String subPropString = subProp.getPropString();

                SubPropAtomicIndices[] atomicsData = new SubPropAtomicIndices[CompoundProposition.propCount];
                SubProposition[] atomics;
                Operator op;

                atomicsData[0] = new SubPropAtomicIndices();
                atomicsData[1] = new SubPropAtomicIndices();
                atomicsData[0].setSuperSubPropString(subPropString);
                atomicsData[1].setSuperSubPropString(subPropString);

                //if (x == 0 && !subProp.getPropString().equals("()"))
                evaluateSubPropsBottomUp(atomicsData);

                atomics = extractAtomicsFromSubProp(subProp, atomicsData, mode);
                op = extractOperatorFromSubProp(subPropString, atomicsData);


                fillInColumn(subProp, atomicsData, atomics, op);

            }
        }
    }

    private Operator extractOperatorFromSubProp(String subPropString, SubPropAtomicIndices[] atomicsData) {
        Operator op = null;
        if (atomicsData[0].getAtomicBeginIndex() > -1 && atomicsData[1].getAtomicBeginIndex() > -1) {
            String opSubstring = subPropString.substring(atomicsData[0].getAtomicEndIndex(), atomicsData[1].getAtomicBeginIndex());
            for (Operator thisOp : Operator.getBinary()) {
                if (opSubstring.contains(thisOp.getSyntax() + "")) {
                    op = thisOp;
                    break;
                }
            }
            if (op == null)
                throw new IllegalStateException("NO RELEVANT OPERATOR FOUND IN SUB_PROP");
        }
        return op;
    }

    private SubProposition[] extractAtomicsFromSubProp(SubProposition subProp, SubPropAtomicIndices[] atomicsData, AtomicProposition.Mode mode) {

        checkAtomicDataState(atomicsData);


        String subPropString = subProp.getPropString();

        SubProposition[] atomics = new SubProposition[atomicsData.length];

        if (atomicsData[0].getAtomicBeginIndex() > -1) {

            if (subPropString.charAt(0) == Operator.NEGATION.getSyntax())
                subProp.setNegation(true);

            // atomicsData.length ist immer 2
            for (int a = 0; a < atomicsData.length; a++) {

                String atomicString;
                boolean atomicNegation = false;

                if (atomicsData[a].getAtomicBeginIndex() > -1) {
                    if (atomicsData[a].getAtomicBeginIndex() > 0)
                        if (subPropString.charAt(atomicsData[a].getAtomicBeginIndex() - 1) == Operator.NEGATION.getSyntax())
                            atomicNegation = true;
                    atomicString = subPropString.substring(atomicsData[a].getAtomicBeginIndex(), atomicsData[a].getAtomicEndIndex());
                    atomics[a] = new SubProposition(atomicString, mode, atomicNegation);

                }
            }

        } else {
            throw noAtomicFoundEx;
        }
        return atomics;
    }

    private void evaluateSubPropsBottomUp(SubPropAtomicIndices[] atomicsData) {

        checkAtomicDataState(atomicsData);

        TableMode tableMode = null;

        String subPropString = atomicsData[0].getSuperSubPropString();

        for (int tm = 0; tm < TableMode.values().length; tm++) {

            if (tm == 0) {
                tableMode = TableMode.SUB;
            } else if (tm == 1) {
                tableMode = TableMode.ATOMIC;
            }

            if (tableMode == TableMode.SUB) {

                // wenn keine vergleichbaren subProps vorhanden sind
                if (subPropTable.get(0).size() <= 1) {
                    continue;
                }
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
            if (atomicsData[1].getAtomicBeginIndex() > -1 && atomicsData[0].getAtomicBeginIndex() > -1)
                break;

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
                    atomicsData[a].setTableMode(tableMode);
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
            SubProposition[] atomics,
            Operator op
    ) {
        checkAtomicDataState(atomicsData);

        // weil by reference scheiße ist
        SubProposition subPropTrue, subPropFalse;
        subPropFalse = new SubProposition(subProp);
        subPropTrue = new SubProposition(subProp);
        if (subProp.isTruth()) {
            subPropFalse.setTruth(false);
        } else {
            subPropTrue.setTruth(true);
        }

        if (atomicsData[1].atomicBeginIndex > -1 || op != null) {
            checkOp(op);
        }


        for (int y = 0; y < rowCount; y++) {

            AtomicProposition[] atomicsFromTableIndices = new AtomicProposition[atomicsData.length];
            String[] newAtomicStrings = new String[atomicsData.length];
            boolean[] atomicsTruth = new boolean[atomicsData.length];
            boolean truth = false; // falscher wert (aufpassen)


            for (int a = 0; a < atomicsData.length; a++) {

                if (a == 1)
                    if (atomicsData[1].atomicBeginIndex <= -1 || op == null)
                        break;

                if (atomicsData[a].getTableMode() == TableMode.ATOMIC) {
                    atomicsFromTableIndices[a] = atomicPropTable[atomicsData[a].getTableXIndex()][y];
                } else if (atomicsData[a].getTableMode() == TableMode.SUB) {
                    atomicsFromTableIndices[a] = subPropTable.get(y).get(atomicsData[a].getTableXIndex());
                }
                newAtomicStrings[a] = atomicsFromTableIndices[a].getPropString();
                atomicsTruth[a] = atomicsFromTableIndices[a].isTruth();


                if (atomics[a].isNegation())
                    atomicsTruth[a] = !atomicsTruth[a];

                if (a == 0)
                    truth = atomicsTruth[a];

                if (atomics[a].getPropString().equals(newAtomicStrings[a])) {
                    if (a == 1)
                        truth = CompoundProposition.getCompoundTruthValue(atomicsTruth[0], op, atomicsTruth[1]);
                } else {
                    throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROP DOES NOT MATCH. THE CURRENT PROP MUST BE DERIVED FROM TABLES PREVIOUS PROPS.");
                }

            }


            if (subProp.isNegation()) {
                truth = !truth;
            }

            if (truth) {
                subPropTable.get(y).add(subPropTrue);
            } else {
                subPropTable.get(y).add(subPropFalse);
            }

        }


    }

    public void checkOp(Operator op) {
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

        int minStringLength = (false + "").length();
        String separator = " | ";

        printTablePropStrings(separator, minStringLength);

        printTruthTable(separator, minStringLength);

    }

    private void printTruthTable(String separator, int minStringLength) {
        int xLength = atomicPropTable.length + subPropTable.get(0).size();
        int yLength = rowCount;

        for (int y = 0; y < yLength; y++) {
            System.out.print(separator);
            for (int x = 0; x < xLength; x++) {

                TableMode tableMode;
                if (x < atomicPropTable.length) {
                    tableMode = TableMode.ATOMIC;
                } else {
                    tableMode = TableMode.SUB;
                }
                printPropTruth(tableMode, new int[]{x, y}, minStringLength, separator);

            }
            System.out.println();
        }
    }

    private void printPropTruth(TableMode tableMode, int[] pos2d, int minStringLength, String separator) {
        int posX = pos2d[0];
        int posY = pos2d[1];
        String tablePosPrintTrue = true + " ";
        String tablePosPrintFalse = false + "";

        AtomicProposition prop = null;
        if (tableMode == TableMode.ATOMIC) {
            prop = atomicPropTable[posX][posY];
        } else if (tableMode == TableMode.SUB) {
            prop = subPropTable.get(posY).get(posX - atomicPropTable.length);
        }

        if (prop.isTruth()) {
            System.out.print(tablePosPrintTrue);
        } else {
            System.out.print(tablePosPrintFalse);
        }
        for (int i = 0; i < prop.getPropString().length() - minStringLength; i++)
            System.out.print(" ");
        System.out.print(separator);

    }

    private void printTablePropStrings(String separator, int minStringLength) {

        System.out.print(separator);
        for (int x = 0; x < atomicPropTable.length + subPropTable.get(0).size(); x++) {

            String propString;
            if (x < atomicPropTable.length) {
                propString = atomicPropTable[x][0].getPropString();
            } else {
                propString = subPropTable.get(0).get(x - atomicPropTable.length).getPropString();
            }
            System.out.print(propString);
            for (int i = 0; i < minStringLength - propString.length(); i++) {
                System.out.print(" ");
            }
            System.out.print(separator);
        }
        System.out.println();

    }

    public void checkAtomicDataState(SubPropAtomicIndices[] atomicsData) {
        if (atomicsData.length != 2)
            throw new IllegalStateException("CRITICAL ERROR: EXACTLY TWO ATOMIC PLACEHOLDERS MUST BE PASSED.");
    }
}
