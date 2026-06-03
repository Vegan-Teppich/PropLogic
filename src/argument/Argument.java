package argument;

import compoundProposition.AtomicProposition;
import compoundProposition.Operator;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Argument {
    private Proposition[] props;
    private Set<AtomicProposition> atomicProps;

    public Argument(String[] premises, String[] conclusions) {
        props = new Proposition[premises.length + conclusions.length];
        for (int i = 0; i < premises.length; i++) {
            props[i] = new Proposition(premises[i], Proposition.Mode.PREMISE, i);
        }
        for (int i = 0; i < conclusions.length; i++) {
            props[premises.length + i] = new Proposition(conclusions[i], Proposition.Mode.CONCLUSION, i);
        }
        this.atomicProps = extractAtomicProps();

    }

    public Argument(String[] premises, String conclusion) {
        this(premises, new String[]{conclusion});
    }

    public Argument(String premise, String[] conclusions) {
        this(new String[]{premise}, conclusions);
    }

    public Argument(String premise, String conclusion) {
        this(new String[]{premise}, new String[]{conclusion});
    }

    public boolean cleanAndCheckIfArgumentIsWellFormedFormula() {
        for (int p = 0; p < props.length; p++) {
            Proposition prop = props[p];

            // ist niemals empty
            if (prop.getPropString().isEmpty())
                return false;

            if (prop.getParenthesesCount()[0] != prop.getParenthesesCount()[1])
                return false;

            if (!checkIfSubPropsAreWellFormedFormula(prop.getSubProps()))
                return false;

        }

        return true;
    }

    public void removeDoubleNegations(){
        for (int p = 0; p < props.length; p++) {
            props[p].setPropString(props[p].removeDoubleNegations());
        }
    }

    private boolean checkIfSubPropsAreWellFormedFormula(List<SubProposition> subProps) {

        for (int p = 0; p < subProps.size(); p++){

            String propString = subProps.get(p).getPropString();
            //System.out.println(propString);
            String lastCompound1 = "";
            String lastCompound2 = "";
            char replaceProp = ' ';

            if (propString.equals(""))
                return false;

            if (propString.charAt(propString.length()-1) != Operator.CLOSING_PARENTHESIS.getSyntax())
                return false;

            if (propString.charAt(0) != Operator.OPENING_PARENTHESIS.getSyntax()){
                if (propString.indexOf(Operator.NEGATION.getSyntax() + "" + Operator.OPENING_PARENTHESIS.getSyntax()) != 0){
                    if (propString.indexOf(Operator.NEGATION.getSyntax() + "" + Operator.NEGATION.getSyntax() + "" + Operator.OPENING_PARENTHESIS.getSyntax()) != 0){
                        return false;
                    }
                }
            }

            if (p >= 1){
                lastCompound1 = subProps.get(p-1).getPropString();
                if (p >= 2){
                    lastCompound2 = subProps.get(p-2).getPropString();
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
                    while ((index = propString.indexOf(op.getSyntax(), index+1)) != -1){
                        opCount++;
                        if (opCount > 1)
                            return false;
                    }

                    if (propString.indexOf(op.getSyntax()) != -1){
                        opIndex = propString.indexOf(op.getSyntax());
                    }

                }
            }

            if (opIndex != -1){
                if (propString.charAt(opIndex-1) == Operator.OPENING_PARENTHESIS.getSyntax() || propString.charAt(opIndex+1) == Operator.CLOSING_PARENTHESIS.getSyntax())
                    return false;

                if (propString.charAt(opIndex-1) == Operator.NEGATION.getSyntax())
                    return false;
            }


            if (propString.contains(replaceProp + "") && propString.length() > 3) {


                if (propString.contains(replaceProp + "" + replaceProp))
                    return false;

                int index = -1;
                int count = 0;
                while ((index = propString.indexOf(replaceProp, index+1)) != -1){
                    count++;
                    if (count > 2)
                        return false;
                }


                if (opIndex-1 == propString.indexOf(replaceProp))
                    if (propString.charAt(propString.indexOf(replaceProp)-1) != Operator.OPENING_PARENTHESIS.getSyntax())
                        return false;

                if (opIndex+1 == propString.lastIndexOf(replaceProp))
                    if (propString.lastIndexOf(replaceProp)+1 != propString.length()-1)
                        return false;

            }
        }

        return true;
    }

    public LinkedHashSet<AtomicProposition> extractAtomicProps() {
        LinkedHashSet<AtomicProposition> atomicProps = new LinkedHashSet<>();

        for (Proposition prop : props){

            for (SubProposition subProp : prop.getSubProps()) {
                String subPropString = subProp.getPropString();

                int index = 0;
                while (index < subPropString.length()) {
                    String currentProp = "";

                    while (index < subPropString.length()) {
                        boolean operatorFoundAtIndex = false;

                        for (Operator op : Operator.values()) {
                            if (subPropString.charAt(index) == op.getSyntax()) {
                                operatorFoundAtIndex = true;
                                break;
                            }
                        }

                        if (!operatorFoundAtIndex) {
                            currentProp += subPropString.charAt(index);
                        } else {
                            index++;
                            break;
                        }
                        index++;

                    }

                    if (!currentProp.isEmpty()) {
                        atomicProps.add(new AtomicProposition(currentProp));
                    }

                }
            }

        }

        return atomicProps;
    }

    public Proposition[] getProps() {
        return props;
    }

    public Set<AtomicProposition> getAtomicProps() {
        return atomicProps;
    }

}
