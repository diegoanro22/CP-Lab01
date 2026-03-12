package dfa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class DFAState {
    private static int counter = 0;

    private final int id;
    private final Set<Integer> positions;
    private boolean accepting;
    private final Map<Character, DFAState> transitions;

    public DFAState(Set<Integer> positions) {
        this.id = counter++;
        this.positions = new TreeSet<>(positions);
        this.accepting = false;
        this.transitions = new HashMap<>();
    }

    public static void resetCounter() {
        counter = 0;
    }

    public int getId() { return id; }

    public Set<Integer> getPositions() { return positions; }

    public boolean isAccepting() { return accepting; }

    public void setAccepting(boolean accepting) { this.accepting = accepting; }

    public void addTransition(char symbol, DFAState target) {
        transitions.put(symbol, target);
    }

    public DFAState getTransition(char symbol) {
        return transitions.get(symbol);
    }

    public Map<Character, DFAState> getTransitions() { return transitions; }

    @Override
    public String toString() {
        return "S" + id + (accepting ? "*" : "") + " " + positions;
    }
}
