
package argument;

import compoundProposition.CompoundProposition;
import compoundProposition.Operator;
import compoundProposition.AtomicProposition;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TruthTable {

    AtomicProposition[][] atomicPropTable;
    List<List<Proposition>> propTable = new ArrayList<>();
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

        for (int y = 0; y < rowCount; y++){
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
            List<String> parenthesesProps = props[p].getParenthesesProps();
            Proposition.Mode mode = props[p].getMode();

            for (x = 0; x < parenthesesProps.size(); x++) {
                String parenthesisProp = parenthesesProps.get(x);


                boolean propNegation = false;
                TableMode atomic1TableMode = null;
                int atomic1TableIndex = -1;
                int[] atomic1ParenthesisPropIndices = {-1, -1};
                String atomic1String;

                TableMode atomic2TableMode = null;
                int atomic2TableIndex = -1;
                int[] atomic2ParenthesisPropIndices = {-1, -1};
                String atomic2String;

                if (parenthesisProp.charAt(0) == Operator.NEGATION.getSyntax())
                    propNegation = true;

                // parenthesesProps (parenthesesProps) code sehr ähnlich deswegen bitte verbessern
                if (x>0){
                    for (int i = x - 1; i >= 0; i--) {
                        String previousParenthesisProp = propTable.get(0).get(i).getPropString();
                        if (atomic1ParenthesisPropIndices[0] == -1) {
                            atomic1ParenthesisPropIndices[0] = parenthesisProp.indexOf(previousParenthesisProp);
                            atomic1ParenthesisPropIndices[1] = atomic1ParenthesisPropIndices[0] + previousParenthesisProp.length();

                            if (atomic1ParenthesisPropIndices[0] != -1) {
                                atomic1TableMode = TableMode.PARENTHESES;
                                atomic1TableIndex = i;
                            }
                        } else if (atomic2ParenthesisPropIndices[0] == -1) {

                            // 2te atomicProp
                            atomic2ParenthesisPropIndices[0] = parenthesisProp.lastIndexOf(previousParenthesisProp);
                            atomic2ParenthesisPropIndices[1] = atomic2ParenthesisPropIndices[0] + previousParenthesisProp.length();

                            int[][] atomicIndices = handleAtomicIndices(atomic1ParenthesisPropIndices, atomic2ParenthesisPropIndices);
                            atomic1ParenthesisPropIndices = atomicIndices[0];
                            atomic2ParenthesisPropIndices = atomicIndices[1];

                            if (atomic2ParenthesisPropIndices[0] != -1) {
                                atomic2TableMode = TableMode.PARENTHESES;
                                atomic2TableIndex = i;
                                break;
                            }


                        }
                    }
                }


                // parenthesesProps (atomicProps) code sehr ähnlich deswegen bitte verbessern
                if (atomic2ParenthesisPropIndices[0] == -1) {
                    for (int i = 0; i < atomicPropTable.length; i++) {
                        if (atomic1ParenthesisPropIndices[0] == -1) {

                            atomic1ParenthesisPropIndices[0] = parenthesisProp.indexOf(atomicPropTable[i][0].getPropString());
                            atomic1ParenthesisPropIndices[1] = atomic1ParenthesisPropIndices[0] + atomicPropTable[i][0].getPropString().length();

                            if (atomic1ParenthesisPropIndices[0] != -1) {
                                atomic1TableMode = TableMode.ATOMIC;
                                atomic1TableIndex = i;
                            }

                            continue;
                        }
                        atomic2ParenthesisPropIndices[0] = parenthesisProp.lastIndexOf(atomicPropTable[i][0].getPropString());
                        atomic2ParenthesisPropIndices[1] = atomic2ParenthesisPropIndices[0] + atomicPropTable[i][0].getPropString().length();

                        int[][] atomicIndices = handleAtomicIndices(atomic1ParenthesisPropIndices, atomic2ParenthesisPropIndices);
                        atomic1ParenthesisPropIndices = atomicIndices[0];
                        atomic2ParenthesisPropIndices = atomicIndices[1];

                        if (atomic2ParenthesisPropIndices[0] != -1) {
                            atomic2TableMode = TableMode.ATOMIC;
                            atomic2TableIndex = i;
                            break;
                        }

                    }
                }

                /*
                System.out.println(parenthesesProps + "\n");
                System.out.println(atomic1TableMode);
                System.out.println(atomic1ParenthesisPropIndices[0]);
                System.out.println(atomic1ParenthesisPropIndices[1]);
                */

                atomic1String = parenthesisProp.substring(atomic1ParenthesisPropIndices[0], atomic1ParenthesisPropIndices[1]);
                atomic2String = parenthesisProp.substring(atomic2ParenthesisPropIndices[0], atomic2ParenthesisPropIndices[1]);

                if (atomic1ParenthesisPropIndices[0] != -1 && atomic2ParenthesisPropIndices[0] != -1) {
                    Operator op = null;
                    String opSubstring = parenthesisProp.substring(atomic1ParenthesisPropIndices[1], atomic2ParenthesisPropIndices[0]);
                    for (Operator thisOp : Operator.getBinary()){
                        if (opSubstring.contains(thisOp.getSyntax() + "")){
                            op = thisOp;
                            break;
                        }
                    }
                    if (op == null)
                        throw new IllegalStateException("NO OPERATOR FOUND");
                    fillInColumn(x, propNegation, mode, atomic1TableMode, atomic1TableIndex, /* atomic1ParenthesisPropIndices, */ atomic1String, op, atomic2TableMode, atomic2TableIndex, /* atomic2ParenthesisPropIndices, */ atomic2String);
                } else if (atomic1ParenthesisPropIndices[0] != -1 && atomic2ParenthesisPropIndices[0] == -1) {
                    fillInColumn(propNegation, atomic1TableMode, atomic1TableIndex, atomic1String);
                } else if (atomic1ParenthesisPropIndices[0] == -1 && atomic2ParenthesisPropIndices[0] == -1) {
                    throw new IllegalStateException("IT IS NOT POSSIBLE FOR A PROPOSITION TO CONSIST OF NOTHING (NOT EVEN A SINGLE ATOMIC PROPOSITION).");
                } else if (atomic1ParenthesisPropIndices[0] == -1 && atomic2ParenthesisPropIndices[0] != -1) {
                    throw new IllegalStateException("IT IS NOT POSSIBLE THAT A SECOND ATOMIC PROPOSITION WAS FOUND, BUT NO FIRST ONE");
                }

            }

        }

    }

    private int[][] handleAtomicIndices(int[] atomic1ParenthesisPropIndices, int[] atomic2ParenthesisPropIndices) {
        SecondAtomicPositionValidity atomic2IndexValid = isAtomic2Valid(atomic1ParenthesisPropIndices, atomic2ParenthesisPropIndices);
        if (atomic2IndexValid == SecondAtomicPositionValidity.INVALID) {
            atomic2ParenthesisPropIndices[0] = -1;
            atomic2ParenthesisPropIndices[1] = -1;
        } else if (atomic2IndexValid == SecondAtomicPositionValidity.FLIP_VALID) {
            int[] flipAtomicIndex = atomic1ParenthesisPropIndices;
            atomic1ParenthesisPropIndices = atomic2ParenthesisPropIndices;
            atomic2ParenthesisPropIndices = flipAtomicIndex;

            /*
            TableMode flipTableMode = atomic1TableMode;
            atomic1TableMode = atomic2TableMode;
            atomic2TableMode = flipTableMode;

            int flipAtomicTableIndex = atomic1TableIndex;
            atomic1TableIndex = atomic2TableIndex;
            atomic2TableIndex = flipAtomicTableIndex;
            */

        } else if (atomic2IndexValid == SecondAtomicPositionValidity.VALID) {

        }

        return new int[][]{atomic1ParenthesisPropIndices, atomic2ParenthesisPropIndices};
    }

    private SecondAtomicPositionValidity isAtomic2Valid(int[] atomic1IndexInParenthesisProp, int[] atomic2IndexInParenthesisProp) {
        if (atomic1IndexInParenthesisProp[1] <= atomic2IndexInParenthesisProp[0]) {
            return SecondAtomicPositionValidity.VALID;
        } else if (atomic2IndexInParenthesisProp[1] <= atomic1IndexInParenthesisProp[0]) {
            return SecondAtomicPositionValidity.FLIP_VALID;
        }
        return SecondAtomicPositionValidity.INVALID;
    }


    private void fillInColumn(
            int propTableXIndex,
            boolean propNegation,
            AtomicProposition.Mode mode,

            TableMode atomic1TableMode,
            int atomic1TableXIndex,
            String atomic1String,

            Operator op,

            TableMode atomic2TableMode,
            int atomic2TableXIndex,
            String atomic2String
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

        for (int y = 0; y < rowCount; y++){

            AtomicProposition atomic1 = null;
            AtomicProposition atomic2 = null;

            String newAtomic1String;
            String newAtomic2String;

            if (atomic1TableMode == TableMode.ATOMIC){
                atomic1 = atomicPropTable[atomic1TableXIndex][y];
            } else if (atomic1TableMode == TableMode.PARENTHESES) {
                atomic1 = propTable.get(y).get(atomic1TableXIndex);
            }
            if (atomic2TableMode == TableMode.ATOMIC){
                atomic2 = atomicPropTable[atomic2TableXIndex][y];
            } else if (atomic2TableMode == TableMode.PARENTHESES) {
                atomic2 = propTable.get(y).get(atomic2TableXIndex);
            }
            newAtomic1String = atomic1.getPropString();
            newAtomic2String = atomic2.getPropString();

            if (atomic1String.equals(newAtomic1String) && atomic2String.equals(newAtomic2String)){
                CompoundProposition cpProp = new CompoundProposition(atomic1, op, atomic2, mode);
                if (propNegation)
                    cpProp.setTruth(!cpProp.isTruth());
                propTable.get(y).add(cpProp);
            }else{
                throw new IllegalStateException("CRITICAL ERROR! ATOMIC_PROPS DO NOT MATCH. THE CURRENT COMPOUND_PROP MUST BE DERIVED FROM A PAIR OF THE TABLES PREVIOUS PROPS.");
            }

        }

    }

    private void fillInColumn(
            boolean propNegation,

            TableMode atomic1TableMode,
            int atomic1TableXIndex,
            String atomic1String
    ){
        for (int y = 0; y < rowCount; y++) {

            AtomicProposition atomic1 = null;
            String newAtomic1String;

            if (atomic1TableMode == TableMode.ATOMIC){
                atomic1 = atomicPropTable[atomic1TableXIndex][y];
            } else if (atomic1TableMode == TableMode.PARENTHESES) {
                atomic1 = propTable.get(y).get(atomic1TableXIndex);
            }

            newAtomic1String = atomic1.getPropString();

            if (atomic1String.equals(newAtomic1String)){
                if (propNegation)
                    atomic1.setTruth(!atomic1.isTruth());
                propTable.get(y).add(new Proposition(atomic1));
            }else {
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
            if (x < atomicPropTable.length){
                propString = atomicPropTable[x][0].getPropString();
            }
            else{
                propString = propTable.get(0).get(x-atomicPropTable.length).getPropString();
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

                if (x< atomicPropTable.length){
                    if (atomicPropTable[x][y].isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);
                }else {
                    if (propTable.get(y).get(x-atomicPropTable.length).isTruth())
                        System.out.print(tablePosPrintTrue);
                    else
                        System.out.print(tablePosPrintFalse);

                }

            }
            System.out.println();
        }
    }
}
