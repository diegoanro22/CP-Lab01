package dfa;

import visitor.FollowPosVisitor;

import java.util.*;

/**
 * Constructs a DFA from the output of FollowPosVisitor using the subset/BFS method.
 *
 * Requires:
 *   - followPos : Map<Integer, Set<Integer>>  (position → set of following positions)
 *   - positionSymbols : Map<Integer, Character> (position → symbol, including '#')
 *   - initialPositions : Set<Integer>          (firstPos of the augmented root)
 *   - hashPosition : int                       (position assigned to '#')
 */
public class DFABuilder {

    private final Map<Integer, Set<Integer>> followPos;
    private final Map<Integer, Character> positionSymbols;
    private final Set<Integer> initialPositions;
    private final int hashPosition;

    public DFABuilder(Map<Integer, Set<Integer>> followPos,
                      Map<Integer, Character> positionSymbols,
                      Set<Integer> initialPositions,
                      int hashPosition) {
        this.followPos = followPos;
        this.positionSymbols = positionSymbols;
        this.initialPositions = initialPositions;
        this.hashPosition = hashPosition;
    }

    public DFA build() {
        DFAState.resetCounter();

        // Collect alphabet (all symbols except '#')
        Set<Character> alphabet = new TreeSet<>();
        for (Map.Entry<Integer, Character> e : positionSymbols.entrySet()) {
            if (e.getValue() != '#') {
                alphabet.add(e.getValue());
            }
        }

        List<DFAState> allStates = new ArrayList<>();
        // Map from position-set → DFAState to avoid duplicates
        Map<Set<Integer>, DFAState> stateMap = new HashMap<>();

        // BFS queue
        Queue<DFAState> queue = new LinkedList<>();

        // Create initial state
        DFAState startState = new DFAState(initialPositions);
        markAccepting(startState);
        allStates.add(startState);
        stateMap.put(new TreeSet<>(initialPositions), startState);
        queue.add(startState);

        while (!queue.isEmpty()) {
            DFAState current = queue.poll();

            for (char sym : alphabet) {
                // Union of followPos(i) for all i in current whose symbol == sym
                Set<Integer> nextPositions = new TreeSet<>();
                for (int pos : current.getPositions()) {
                    if (positionSymbols.getOrDefault(pos, '\0') == sym) {
                        Set<Integer> fp = followPos.get(pos);
                        if (fp != null) nextPositions.addAll(fp);
                    }
                }

                if (nextPositions.isEmpty()) continue;

                DFAState target = stateMap.get(nextPositions);
                if (target == null) {
                    target = new DFAState(nextPositions);
                    markAccepting(target);
                    allStates.add(target);
                    stateMap.put(new TreeSet<>(nextPositions), target);
                    queue.add(target);
                }
                current.addTransition(sym, target);
            }
        }

        return new DFA(startState, allStates, alphabet);
    }

    private void markAccepting(DFAState state) {
        if (state.getPositions().contains(hashPosition)) {
            state.setAccepting(true);
        }
    }
}
