import java.util.Map;
import java.util.Set;

/** Represents a deterministic finite automaton built from a regular expression. */
public class DFA {

    /** The initial state of the DFA. */
    public DFAState initialState;

    /** The set of accepting states in the DFA. */
    public Set<DFAState> acceptingStates;

    /** The transition table mapping each state and input symbol to the next state. */
    public Map<DFAState, Map<Character, DFAState>> transitionTable;

    /** Prints the DFA transition table to standard output. */
    public void printTable() {
        // TODO: implement
    }
}
