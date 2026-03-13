package simulation;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;

import dfa.DFA;
import dfa.DFAState;

public class DFASimulator {

    private final DFA dfa;

    public DFASimulator(DFA dfa) {
        this.dfa = dfa;
    }

    /**
     * Simulates the DFA on the given input string.
     * Prints each step: current state + symbol consumed → next state.
     *
     * @param input the string to test
     * @return true if the string is accepted, false otherwise
     */
    public boolean simulate(String input) {
        DFAState current = dfa.getInitialState();
        System.out.println("Simulando: \"" + input + "\"");
        System.out.println("  Estado inicial: S" + current.getId());

        for (int i = 0; i < input.length(); i++) {
            char sym = input.charAt(i);
            DFAState next = current.getTransition(sym);
            if (next == null) {
                System.out.println("  S" + current.getId() + " --[" + sym + "]--> (sin transición) → RECHAZADO");
                return false;
            }
            System.out.println("  S" + current.getId() + " --[" + sym + "]--> S" + next.getId());
            current = next;
        }

        boolean accepted = current.isAccepting();
        System.out.println("  Estado final: S" + current.getId()
                + (accepted ? " (aceptación)" : " (no aceptación)"));
        return accepted;
    }
}
