import java.util.ArrayList;

public class Objective {
    Node target;                // The target node for this objective
    ArrayList<Integer> helpOptions; // Options for wizard's help
    boolean offersHelp = false;         // Whether wizard offers help for this objective

    // Constructor
    public Objective(Node target, ArrayList<Integer> helpOptions, boolean offersHelp) {
        this.target = target;
        this.helpOptions = helpOptions;
        this.offersHelp = offersHelp;
    }

    // String representation for debugging
    @Override
    public String toString() {
        return "Objective(target=" + target + ", offersHelp=" + offersHelp + ", helpOptions=" + helpOptions + ")";
    }
}
