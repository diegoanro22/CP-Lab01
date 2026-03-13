package dfa;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;

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
