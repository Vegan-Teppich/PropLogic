package argument;

import compoundProposition.AtomicProposition;
import compoundProposition.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proposition extends AtomicProposition {
    private List<SubProposition> subProps;

    private int[] parenthesesCount;
    protected Mode mode;
    protected int propIndex;

    //private int[] index = new int[2];
    public Proposition(String propString, Mode mode, int propIndex) {
        super(propString);
        this.mode = mode;
        this.propIndex = propIndex;
        init();
    }

    private void init(){
        //if (propString.isEmpty())
        //    return;
        this.propString = cleanPropString();
        this.parenthesesCount = countParentheses();
        this.subProps = extractSubProps();
    }



    public String cleanPropString() {

        String propString = this.propString.replace(" ", "");


        if (propString.isEmpty()){
            propString = Operator.OPENING_PARENTHESIS.getSyntax() + propString + Operator.CLOSING_PARENTHESIS.getSyntax();
            return propString;
        }else {
            if (propString.charAt(0) != Operator.OPENING_PARENTHESIS.getSyntax() || propString.charAt(propString.length() - 1) != Operator.CLOSING_PARENTHESIS.getSyntax())
                if (!(propString.charAt(0) == Operator.NEGATION.getSyntax() && propString.charAt(propString.length() - 1) != Operator.CLOSING_PARENTHESIS.getSyntax()))
                    propString = Operator.OPENING_PARENTHESIS.getSyntax() + propString + Operator.CLOSING_PARENTHESIS.getSyntax();
        }

        // remove unnecessary double negations
        int searchIndex = 0;
        while ((searchIndex = propString.indexOf(Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax(), searchIndex)) != -1) {
            propString = propString.substring(0, searchIndex) + propString.substring(searchIndex + 2);
        }
        return propString;
    }

    public int[] countParentheses() {
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

    public ArrayList<SubProposition> extractSubProps() {
        ArrayList<SubProposition> output = new ArrayList<>();

        int openingParenthesisIndex = -1;
        int closingParenthesisIndex = -1;
        ArrayList<Integer> usedOpeningParenthesesIndices = new ArrayList<>();

        closingParenthesisIndex--; // wichtig
        for (int i = 0; i < parenthesesCount[1]; i++) {
            boolean subPropIsFullProp = false;

            // handle closingParentheses
            if ((closingParenthesisIndex = propString.indexOf(Operator.CLOSING_PARENTHESIS.getSyntax(), closingParenthesisIndex + 1)) != -1)
                openingParenthesisIndex = closingParenthesisIndex;

            // handle openingParentheses und speicher benutzte
            for (int j = closingParenthesisIndex; j >= 0; j--) {
                if (propString.charAt(j) == Operator.OPENING_PARENTHESIS.getSyntax()) {
                    boolean parenthesisWasUsed = false;
                    for (int usedOpeningParenthesisIndex : usedOpeningParenthesesIndices) {
                        if (j == usedOpeningParenthesisIndex) {
                            parenthesisWasUsed = true;
                            break;
                        }

                    }
                    if (!parenthesisWasUsed) {
                        openingParenthesisIndex = j;
                        usedOpeningParenthesesIndices.add(j);
                        break;
                    }
                }
            }

            // output
            //output.add(propString.substring(openingParenthesisIndex, closingParenthesisIndex+1));

            // wichtig: nachprüfen ob eine spätere negation auf eine jetzige überspringt
            boolean negationExists = false;
            while (openingParenthesisIndex > 0) {
                if (propString.charAt(openingParenthesisIndex - 1) == Operator.NEGATION.getSyntax()) {
                    openingParenthesisIndex--;
                    negationExists = true;
                    continue;
                }
                break;
            }
            String subPropString = propString.substring(openingParenthesisIndex, closingParenthesisIndex + 1);
            //if (negationExists)
            if (i == parenthesesCount[1]-1)
                subPropIsFullProp = true;
            output.add(new SubProposition(subPropString, mode, negationExists, propIndex, subPropIsFullProp));

        }
        return output;
    }



    public String removeDoubleNegations() {
        String propString = this.getPropString();

        char negation = Operator.NEGATION.getSyntax();
        while (propString.contains(negation + "" + negation)) {
            propString = propString.replace(negation + "" + negation, "");
        }
        return propString;
    }

    @Override
    public int hashCode(){
        return Objects.hash(propString, mode);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj.getClass() == this.getClass()){
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
        return parenthesesCount;
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
