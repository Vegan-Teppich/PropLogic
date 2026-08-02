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
                        atomicProps.add(new AtomicProposition(currentProp, false, null, -1));
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
