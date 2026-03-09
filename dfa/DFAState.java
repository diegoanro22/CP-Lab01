import java.util.Set;

/** Represents a single state in the DFA. */
public class DFAState {

    /** The human-readable name of this state. */
    public String name;

    /** The set of regex positions represented by this state. */
    public Set<Integer> positions;

    /** True if this state is an accepting state. */
    public boolean isAccepting;
}
