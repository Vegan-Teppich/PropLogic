package argument;

import compoundProposition.AtomicProposition;
import compoundProposition.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proposition extends AtomicProposition {
    private List<SubProposition> subProps;

    protected Mode mode;
    protected int propIndex;
    int[] customParenthesesCount;

    boolean wellFormedFormula = true;

    //private int[] index = new int[2];
    public Proposition(String propString, Mode mode, int propIndex) {
        super(propString);
        this.mode = mode;
        this.propIndex = propIndex;
        init();
    }

    private void init() {
        this.wellFormedFormula = isWffPreCheck();
        this.propString = cleanPropString();
        this.wellFormedFormula = checkIfWffAndExtractSubProps();

        this.propString = removeAllDoubleNegations();

    }

    private boolean isWffPreCheck() {
        if (propString.isEmpty())
            return false;
        if (!isCustomParenthesesCountEqual())
            return false;
        return wellFormedFormula;
    }

    private boolean isCustomParenthesesCountEqual(){
        customParenthesesCount = countCustomParentheses();
        return customParenthesesCount[0] == customParenthesesCount[1];
    }


    public String cleanPropString() {

        String propString = this.propString.replace(" ", "");

        propString = removeUnusableDoubleNegations(propString);

        return propString;
    }



    public String removeUnusableDoubleNegations(String propString) {

        int searchIndex = 0;
        while ((searchIndex = propString.indexOf(Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax(), searchIndex)) != -1) {
            propString = propString.substring(0, searchIndex) + propString.substring(searchIndex + 2);
        }
        return propString;

    }


    public int[] countCustomParentheses() {
        int openingParenthesesNumber = 0;
        int closingParenthesesNumber = 0;

        int searchIndex = -1;
        while ((searchIndex = propString.indexOf(Operator.OPENING_PARENTHESIS.getSyntax(), searchIndex + 1)) != -1) {
            openingParenthesesNumber++;
        }
        while ((searchIndex = propString.indexOf(Operator.CLOSING_PARENTHESIS.getSyntax(), searchIndex + 1)) != -1) {
            closingParenthesesNumber++;
        }
        return new int[]{openingParenthesesNumber, closingParenthesesNumber};
    }

    public void extractSubPropsAndAddParentheses() {
        propString = Operator.OPENING_PARENTHESIS.getSyntax() + propString + Operator.CLOSING_PARENTHESIS.getSyntax();

        int openingParenthesisIndex = -1;
        int closingParenthesisIndex = -1;
        ArrayList<Integer> usedOpeningParenthesesIndices = new ArrayList<>();

        closingParenthesisIndex--; // wichtig damit beim + rechnen -1 für den ersten check rauskommt
        for (int i = 0; i < customParenthesesCount[1]; i++) {

            // setze openingParenthesisIndex zum neuen index zurück
            if ((closingParenthesisIndex = propString.indexOf(Operator.CLOSING_PARENTHESIS.getSyntax(), closingParenthesisIndex + 1)) != -1)
                openingParenthesisIndex = closingParenthesisIndex;

            openingParenthesisIndex = searchNextOpeningParenthesis(closingParenthesisIndex, usedOpeningParenthesesIndices);
            if (openingParenthesisIndex > -1)
                usedOpeningParenthesesIndices.add(openingParenthesisIndex);

            // output
            //output.add(propString.substring(openingParenthesisIndex, closingParenthesisIndex+1));

            boolean negationExists = isSubPropNegated(openingParenthesisIndex);
            if (negationExists)
                // offset für die negation
                openingParenthesisIndex--;

            String subPropString = propString.substring(openingParenthesisIndex, closingParenthesisIndex + 1);

            subProps.add(new SubProposition(subPropString, mode, negationExists, propIndex, isSubPropFullProp(i)));

        }
    }

    private boolean isSubPropFullProp(int currentParenthesisCount) {
        return currentParenthesisCount == customParenthesesCount[1] - 1;
    }

    private boolean isSubPropNegated(int openingParenthesisIndex) {
        if (openingParenthesisIndex >= 1) {
            if (openingParenthesisIndex >= 2) {
                if (propString.substring(openingParenthesisIndex - 1, openingParenthesisIndex).equals(Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax())) {
                    return false;
                }
            }
            if (propString.charAt(openingParenthesisIndex - 1) == Operator.NEGATION.getSyntax()) {
                return true;
            }
        }
        return false;
    }

    private int searchNextOpeningParenthesis(int closingParenthesisIndex, List<Integer> usedOpeningParenthesesIndices) {
        int foundOpeningParenthesisIndex;
        while ((foundOpeningParenthesisIndex = propString.substring(0, closingParenthesisIndex).lastIndexOf(Operator.OPENING_PARENTHESIS.getSyntax())) != -1) {
            boolean parenthesisWasUsed = false;
            for (int usedOpeningParenthesisIndex : usedOpeningParenthesesIndices) {
                if (foundOpeningParenthesisIndex == usedOpeningParenthesisIndex) {
                    parenthesisWasUsed = true;
                    break;
                }
            }
            if (!parenthesisWasUsed) {
                return foundOpeningParenthesisIndex;
            }

        }
        return foundOpeningParenthesisIndex;
    }

    public boolean checkIfWffAndExtractSubProps() {

        extractSubPropsAndAddParentheses();

        if (!checkIfSubPropsAreWellFormedFormula())
            return false;
        return true;

    }


    private boolean checkIfSubPropsAreWellFormedFormula() {

        for (int p = 0; p < subProps.size(); p++) {

            String propString = subProps.get(p).getPropString();
            //System.out.println(propString);
            String lastCompound1 = "";
            String lastCompound2 = "";
            char replaceProp = ' ';


            if (p >= 1) {
                lastCompound1 = subProps.get(p - 1).getPropString();
                if (p >= 2) {
                    lastCompound2 = subProps.get(p - 2).getPropString();
                }
            }


            if (!lastCompound1.isEmpty())
                propString = propString.replace(lastCompound1, replaceProp + "");
            if (!lastCompound2.isEmpty())
                propString = propString.replace(lastCompound2, replaceProp + "");


            int opCount = 0;
            int opIndex = -1;
            for (Operator op : Operator.values()) {
                if (op == Operator.OPENING_PARENTHESIS || op == Operator.CLOSING_PARENTHESIS)
                    continue;

                if (propString.charAt(propString.length() - 2) == op.getSyntax())
                    return false;

                if (op == Operator.NEGATION)
                    continue;

                if (propString.charAt(1) == op.getSyntax() || propString.charAt(propString.length() - 1) == op.getSyntax() || propString.charAt(0) == op.getSyntax())
                    return false;

                if (propString.contains(op.getSyntax() + "")) {

                    int index = -1;
                    while ((index = propString.indexOf(op.getSyntax(), index + 1)) != -1) {
                        opCount++;
                        if (opCount > 1)
                            return false;
                    }

                    if (propString.indexOf(op.getSyntax()) != -1) {
                        opIndex = propString.indexOf(op.getSyntax());
                    }

                }
            }

            if (opIndex != -1) {
                if (propString.charAt(opIndex - 1) == Operator.OPENING_PARENTHESIS.getSyntax() || propString.charAt(opIndex + 1) == Operator.CLOSING_PARENTHESIS.getSyntax())
                    return false;

                if (propString.charAt(opIndex - 1) == Operator.NEGATION.getSyntax())
                    return false;
            }


            if (propString.contains(replaceProp + "") && propString.length() > 3) {


                if (propString.contains(replaceProp + "" + replaceProp))
                    return false;

                int index = -1;
                int count = 0;
                while ((index = propString.indexOf(replaceProp, index + 1)) != -1) {
                    count++;
                    if (count > 2)
                        return false;
                }


                if (opIndex - 1 == propString.indexOf(replaceProp))
                    if (propString.charAt(propString.indexOf(replaceProp) - 1) != Operator.OPENING_PARENTHESIS.getSyntax())
                        return false;

                if (opIndex + 1 == propString.lastIndexOf(replaceProp))
                    if (propString.lastIndexOf(replaceProp) + 1 != propString.length() - 1)
                        return false;

            }
        }

        return true;
    }


    public String removeAllDoubleNegations() {
        String propString = this.getPropString();

        char negation = Operator.NEGATION.getSyntax();
        while (propString.contains(negation + "" + negation)) {
            propString = propString.replace(negation + "" + negation, "");
        }
        return propString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(propString, mode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj.getClass() == this.getClass()) {
            if (((Proposition) obj).propString.equals(this.propString) && ((Proposition) obj).mode == this.mode)
                return true;
        }
        return false;
    }

    public String toString() {
        return getClass().getSimpleName() + ": " + mode + " " + propString;
    }

    @Override
    public void setPropString(String propString) {
        super.setPropString(propString);
        init();
    }

    public int[] getParenthesesCount() {
        // 0 = open | 1 = close
        return customParenthesesCount;
    }

    public List<SubProposition> getSubProps() {
        return subProps;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /*
    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        if (!propStringCleaned)
            throw notCleanException;
        this.index = index;
    }
    */
}
