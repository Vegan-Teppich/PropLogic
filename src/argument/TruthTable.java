package argument;

import compoundProposition.CompoundProposition;
import compoundProposition.Operator;
import compoundProposition.AtomicProposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TruthTable {

    AtomicProposition[][] atomicPropTable;
    List<List<SubProposition>> propTable = new ArrayList<>();
    int rowCount;

    public TruthTable(Argument arg) {

        fillInTable(arg);
    }

    private void fillInTable(Argument arg) {

        Proposition[] props = arg.getProps();


        Set<AtomicProposition> atomicProps = arg.getAtomicProps();


        rowCount = 1;
        for (int x = 0; x < atomicProps.size(); x++) {
            rowCount = rowCount * 2;
        }
        atomicPropTable = new AtomicProposition[atomicProps.size()][rowCount];

        for (int y = 0; y < rowCount; y++) {
            propTable.add(new ArrayList<>());
        }

        // atomicProps
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


        // parenthesesProps
        for (int p = 0; p < props.length; p++) {

            List<SubProposition> subProps = props[p].getSubProps();
            AtomicProposition.Mode mode = props[p].getMode();
            for (x = 0; x < subProps.size(); x++) {
                SubProposition subProp = subProps.get(x);

                String subPropString = subProp.getPropString();
                boolean parenthesesNegation = false;

                System.out.println(subPropString);

                boolean atomic1Negation = false;
                TableMode atomic1TableMode = null;
                int atomic1TableIndex = -1;
                int[] atomic1SubPropIndices = {-1, -1};
                String atomic1String;

                boolean atomic2Negation = false;
                TableMode atomic2TableMode = null;
                int atomic2TableIndex = -1;
                int[] atomic2SubPropIndices = {-1, -1};
                String atomic2String;

                if (subPropString.charAt(0) == Operator.NEGATION.getSyntax())
                    parenthesesNegation = true;

                // subProps (subProps) code sehr ähnlich deswegen bitte verbessern
                // wenn vergleichbare subProps vorhanden sind
                if (x > 0) {
                    for (int i = x - 1; i >= 0; i--) {
                        String previousParenthesisProp = propTable.get(0).get(i).getPropString();
                        if (atomic1SubPropIndices[0] == -1 || atomic1SubPropIndices[1] == -1) {
                            atomic1SubPropIndices[0] = subPropString.indexOf(previousParenthesisProp);
                            atomic1SubPropIndices[1] = atomic1SubPropIndices[0] + previousParenthesisProp.length();

                            if (atomic1SubPropIndices[0] != -1) {
                                atomic1TableMode = TableMode.PARENTHESES;
                                atomic1TableIndex = i;
                            }
                            continue;
                        }

                        /*
                        int testIndex1 = subPropString.lastIndexOf(previousParenthesisProp);
                        int testIndex2 = testIndex1 + previousParenthesisProp.length();

                        if ((testIndex1 > atomic1SubPropIndices[0] && testIndex1 < atomic1SubPropIndices[1]) || (testIndex2 > atomic1SubPropIndices[0] && testIndex2 < atomic1SubPropIndices[1]))
                            continue;

                        atomic2SubPropIndices[0] = testIndex1;
                        atomic2SubPropIndices[1] = testIndex2;
                        */

                        // 2te atomicProp
                        atomic2SubPropIndices[0] = subPropString.lastIndexOf(previousParenthesisProp);
                        atomic2SubPropIndices[1] = atomic2SubPropIndices[0] + previousParenthesisProp.length();

                        if (atomic2SubPropIndices[0] != -1) {
                            atomic2TableMode = TableMode.PARENTHESES;
                            atomic2TableIndex = i;
                        }

                        SecondAtomicPositionValidity atomic2IndexValid = isAtomic2Valid(atomic1SubPropIndices, atomic2SubPropIndices);

                        if (atomic2IndexValid == SecondAtomicPositionValidity.INVALID) {
                            atomic2SubPropIndices[0] = -1;
                            atomic2SubPropIndices[1] = -1;
                        } else if (atomic2IndexValid == SecondAtomicPositionValidity.FLIP_VALID) {
                            int[] flipAtomicIndex = atomic1SubPropIndices;
                            atomic1SubPropIndices = atomic2SubPropIndices;
                            atomic2SubPropIndices = flipAtomicIndex;


                            TableMode flipAtomicTableMode = atomic1TableMode;
                            atomic1TableMode = atomic2TableMode;
                            atomic2TableMode = flipAtomicTableMode;

                            int flipAtomicTableIndex = atomic1TableIndex;
                            atomic1TableIndex = atomic2TableIndex;
                            atomic2TableIndex = flipAtomicTableIndex;

                            break;
                        } else if (atomic2IndexValid == SecondAtomicPositionValidity.VALID) {
                            break;
                        }



                    }


                }


                // subProps (atomicProps) code sehr ähnlich deswegen bitte verbessern
                if (atomic2SubPropIndices[0] == -1) {
                    for (int i = 0; i < atomicPropTable.length; i++) {
                        if (atomic1SubPropIndices[0] == -1 || atomic1SubPropIndices[1] == -1) {

                            atomic1SubPropIndices[0] = subPropString.indexOf(atomicPropTable[i][0].getPropString());
                            atomic1SubPropIndices[1] = atomic1SubPropIndices[0] + atomicPropTable[i][0].getPropString().length();

                            // kann man auch am ende checken
                            if (atomic1SubPropIndices[0] != -1) {
                                atomic1TableMode = TableMode.ATOMIC;
                                atomic1TableIndex = i;
                            }
                            continue;
                        }

                        /*
                        int testIndex1 = subPropString.lastIndexOf(atomicPropTable[i][0].getPropString());
                        int testIndex2 = testIndex1 + atomicPropTable[i][0].getPropString().length();

                        if ((testIndex1 > atomic1SubPropIndices[0] && testIndex1 < atomic1SubPropIndices[1]) || (testIndex2 > atomic1SubPropIndices[0] && testIndex2 < atomic1SubPropIndices[1]))
                            continue;

                        atomic2SubPropIndices[0] = testIndex1;
                        atomic2SubPropIndices[1] = testIndex2;
                        */

                        atomic2SubPropIndices[0] = subPropString.lastIndexOf(atomicPropTable[i][0].getPropString());
                        atomic2SubPropIndices[1] = atomic2SubPropIndices[0] + atomicPropTable[i][0].getPropString().length();

                        if (atomic2SubPropIndices[0] != -1) {
                            atomic2TableMode = TableMode.ATOMIC;
                            atomic2TableIndex = i;
                        }

                        SecondAtomicPositionValidity atomic2IndexValid = isAtomic2Valid(atomic1SubPropIndices, atomic2SubPropIndices);
                        if (atomic2IndexValid == SecondAtomicPositionValidity.INVALID) {
                            atomic2SubPropIndices[0] = -1;
                            atomic2SubPropIndices[1] = -1;
                        } else if (atomic2IndexValid == SecondAtomicPositionValidity.FLIP_VALID) {
                            int[] flipAtomicIndex = atomic1SubPropIndices;
                            atomic1SubPropIndices = atomic2SubPropIndices;
                            atomic2SubPropIndices = flipAtomicIndex;


                            TableMode flipAtomicTableMode = atomic1TableMode;
                            atomic1TableMode = atomic2TableMode;
                            atomic2TableMode = flipAtomicTableMode;

                            int flipAtomicTableIndex = atomic1TableIndex;
                            atomic1TableIndex = atomic2TableIndex;
                            atomic2TableIndex = flipAtomicTableIndex;
                            break;

                        } else if (atomic2IndexValid == SecondAtomicPositionValidity.VALID) {
                            break;
                        }

                    }
                }

                System.out.println("atomic1:");
                System.out.println(atomic1SubPropIndices[0]);
                System.out.println(atomic1SubPropIndices[1]);
                System.out.println("atomic2");
                System.out.println(atomic2SubPropIndices[0]);
                System.out.println(atomic2SubPropIndices[1]);

                if (atomic1SubPropIndices[0] != -1 && atomic1SubPropIndices[1] != -1){

                    if (atomic1SubPropIndices[0] > 0)
                        if (subPropString.charAt(atomic1SubPropIndices[0]-1) == Operator.NEGATION.getSyntax())
                            atomic1Negation = true;

                    atomic1String = subPropString.substring(atomic1SubPropIndices[0], atomic1SubPropIndices[1]);
                    SubProposition atomic1 = new SubProposition(atomic1String, mode, atomic1Negation);

                    if (atomic2SubPropIndices[0] != -1 && atomic2SubPropIndices[1] != -1){

                        if (atomic2SubPropIndices[0] > 0)
                            if (subPropString.charAt(atomic2SubPropIndices[0]-1) == Operator.NEGATION.getSyntax())
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
                        fillInColumn(subProp,   atomic1, atomic1TableMode, atomic1TableIndex,   op,   atomic2, atomic2TableMode, atomic2TableIndex);
                    }else {
                        fillInColumn(subProp, atomic1, atomic1TableMode, atomic1TableIndex);
                    }
                }


            }
        }
    }


    private SecondAtomicPositionValidity isAtomic2Valid(int[] atomic1IndexInParenthesisProp, int[] atomic2IndexInParenthesisProp) {
        if (atomic2IndexInParenthesisProp[0] == -1 || atomic2IndexInParenthesisProp[1] == -1)
            return SecondAtomicPositionValidity.INVALID;
        if (atomic1IndexInParenthesisProp[1] <= atomic2IndexInParenthesisProp[0]) {
            return SecondAtomicPositionValidity.VALID;
        } else if (atomic2IndexInParenthesisProp[1] <= atomic1IndexInParenthesisProp[0]) {
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
            } else if (atomic1TableMode == TableMode.PARENTHESES) {
                atomic1FromTableIndex = propTable.get(y).get(atomic1TableXIndex);
            }
            if (atomic2TableMode == TableMode.ATOMIC) {
                atomic2FromTableIndex = atomicPropTable[atomic2TableXIndex][y];
            } else if (atomic2TableMode == TableMode.PARENTHESES) {
                atomic2FromTableIndex = propTable.get(y).get(atomic2TableXIndex);
            }
            newAtomic1String = atomic1.getPropString();
            newAtomic2String = atomic2.getPropString();

/*
            System.out.println("atomic1");
            System.out.println(atomic1String);
            System.out.println(newAtomic1String);
            System.out.println();
            System.out.println("atomic2");
            System.out.println(atomic2String);
            System.out.println(newAtomic2String);
*/

            if (atomic1.getPropString().equals(newAtomic1String) && atomic2.getPropString().equals(newAtomic2String)) {
                boolean cpTruth = CompoundProposition.getCompoundTruthValue(atomic1FromTableIndex.isTruth(), op, atomic2FromTableIndex.isTruth());
                if (subProp.isNegation()){
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
            } else if (atomic1TableMode == TableMode.PARENTHESES) {
                atomic1FromTableIndex = propTable.get(y).get(atomic1TableXIndex);
            }

            newAtomic1String = atomic1FromTableIndex.getPropString();

            if (atomic1.getPropString().equals(newAtomic1String)) {
                if (subProp.isNegation())
                    atomic1.setTruth(!atomic1.isTruth());
                propTable.get(y).add(subProp);
            } else {
                throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROP DOES NOT MATCH. THE CURRENT PROP MUST BE DERIVED FROM TABLES PREVIOUS PROPS.");
            }

        }
    }

    public enum TableMode {
        ATOMIC,
        PARENTHESES
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
