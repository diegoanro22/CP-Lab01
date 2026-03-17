package dfa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DFA {
    private final DFAState initialState;
    private final List<DFAState> states;
    private final Set<Character> alphabet;

    public DFA(DFAState initialState, List<DFAState> states, Set<Character> alphabet) {
        this.initialState = initialState;
        this.states = states;
        this.alphabet = new TreeSet<>(alphabet);
    }

    public DFAState getInitialState() { return initialState; }

    public List<DFAState> getStates() { return states; }

    public Set<Character> getAlphabet() { return alphabet; }

    public int countStates() {
        return states.size();
    }

    public int countTransitions() {
        int totalTransitions = 0;
        for (DFAState state : states) {
            totalTransitions += state.getTransitions().size();
        }
        return totalTransitions;
    }

    public void printSummary(String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        System.out.println("Estados: " + countStates());
        System.out.println("Transiciones: " + countTransitions());
        System.out.println("Alfabeto: " + alphabet);
        System.out.println("Estado inicial: S" + initialState.getId());
    }

    public void printComparison(String tituloActual, DFA otro, String tituloOtro) {
        int currentStates = countStates();
        int otherStates = otro.countStates();
        int currentTransitions = countTransitions();
        int otherTransitions = otro.countTransitions();

        System.out.println("\n--- Comparación de AFD ---");
        System.out.println("Estados (" + tituloActual + " -> " + tituloOtro + "): "
                + currentStates + " -> " + otherStates);
        System.out.println("Transiciones (" + tituloActual + " -> " + tituloOtro + "): "
                + currentTransitions + " -> " + otherTransitions);
        System.out.println("Reducción de estados: " + (currentStates - otherStates));
        System.out.println("Reducción de transiciones: " + (currentTransitions - otherTransitions));
    }

    /**
     * Prints the DFA transition table to stdout.
     * Format:
     *   Estado    | <sym1>  <sym2> ...
     *   S0 {..}   |   S1     S2   ...
     */
    public void printTable() {
        // Column widths
        int stateColWidth = 20;
        List<Character> symbols = new ArrayList<>(alphabet);

        // Header
        StringBuilder header = new StringBuilder();
        header.append(padRight("Estado", stateColWidth)).append("|");
        for (char sym : symbols) {
            header.append(padCenter(String.valueOf(sym), 10));
        }
        System.out.println(header);
        System.out.println("-".repeat(stateColWidth + 1 + symbols.size() * 10));

        // Rows
        for (DFAState state : states) {
            String stateLabel = "S" + state.getId()
                    + (state.isAccepting() ? "*" : " ")
                    + " " + state.getPositions();
            StringBuilder row = new StringBuilder();
            row.append(padRight(stateLabel, stateColWidth)).append("|");
            for (char sym : symbols) {
                DFAState target = state.getTransition(sym);
                String cell = (target != null) ? "S" + target.getId() : "-";
                row.append(padCenter(cell, 10));
            }
            System.out.println(row);
        }
        System.out.println();
        System.out.println("(*) = estado de aceptación");
        System.out.println("Estado inicial: S" + initialState.getId());
    }

    // ---- Helpers ----

    private static String padRight(String s, int n) {
        if (s.length() >= n) return s.substring(0, n);
        return s + " ".repeat(n - s.length());
    }

    private static String padCenter(String s, int n) {
        if (s.length() >= n) return " " + s + " ";
        int totalPad = n - s.length();
        int left = totalPad / 2;
        int right = totalPad - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }
}
