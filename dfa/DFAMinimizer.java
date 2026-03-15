package dfa;

import java.util.*;

/**
 * Minimiza un AFD usando el algoritmo de refinamiento de particiones.
 *
 * Pasos:
 *  1. Separar estados en dos bloques iniciales: aceptación y no aceptación.
 *  2. Refinar iterativamente: si dos estados dentro del mismo bloque
 *     tienen transiciones hacia bloques distintos para algún símbolo,
 *     se separan en bloques distintos.
 *  3. Con los bloques estables, construir el AFD minimizado:
 *     cada bloque se convierte en un solo estado nuevo.
 */
public class DFAMinimizer {

    public DFA minimize(DFA original) {
        List<DFAState> states = original.getStates();
        Set<Character> alphabet = original.getAlphabet();

        // Paso 1: partición inicial
        List<Set<DFAState>> partitions = initializePartitions(states);

        // Paso 2: refinamiento iterativo
        boolean changed = true;
        while (changed) {
            changed = false;
            List<Set<DFAState>> newPartitions = new ArrayList<>();
            Map<DFAState, Integer> blockIndex = buildBlockIndex(partitions);

            for (Set<DFAState> block : partitions) {
                // Agrupar estados del bloque por firma de transición
                Map<List<Integer>, Set<DFAState>> groups = new LinkedHashMap<>();
                for (DFAState state : block) {
                    List<Integer> sig = buildSignature(state, alphabet, blockIndex);
                    groups.computeIfAbsent(sig, k -> new LinkedHashSet<>()).add(state);
                }
                newPartitions.addAll(groups.values());
                if (groups.size() > 1) changed = true;
            }
            partitions = newPartitions;
        }

        // Paso 3: construir AFD minimizado
        return buildMinimizedDFA(original, partitions, alphabet);
    }

    /**
     * Partición inicial: un bloque para estados de aceptación,
     * otro para los demás.
     */
    private List<Set<DFAState>> initializePartitions(List<DFAState> states) {
        Set<DFAState> accepting    = new LinkedHashSet<>();
        Set<DFAState> nonAccepting = new LinkedHashSet<>();
        for (DFAState s : states) {
            if (s.isAccepting()) accepting.add(s);
            else                  nonAccepting.add(s);
        }
        List<Set<DFAState>> partitions = new ArrayList<>();
        if (!accepting.isEmpty())    partitions.add(accepting);
        if (!nonAccepting.isEmpty()) partitions.add(nonAccepting);
        return partitions;
    }

    /**
     * Construye un mapa estado → índice de bloque para calcular firmas.
     */
    private Map<DFAState, Integer> buildBlockIndex(List<Set<DFAState>> partitions) {
        Map<DFAState, Integer> index = new HashMap<>();
        for (int i = 0; i < partitions.size(); i++) {
            for (DFAState s : partitions.get(i)) {
                index.put(s, i);
            }
        }
        return index;
    }

    /**
     * Firma de un estado: para cada símbolo del alfabeto (en orden),
     * el índice del bloque destino, o -1 si no hay transición.
     * Dos estados con la misma firma se comportan igual bajo el bloque actual.
     */
    private List<Integer> buildSignature(DFAState state,
                                         Set<Character> alphabet,
                                         Map<DFAState, Integer> blockIndex) {
        List<Integer> sig = new ArrayList<>();
        for (char sym : alphabet) {
            DFAState target = state.getTransition(sym);
            sig.add(target != null ? blockIndex.getOrDefault(target, -1) : -1);
        }
        return sig;
    }

    /**
     * Con los bloques estables, crea el AFD minimizado:
     * - Un estado nuevo por bloque.
     * - Transiciones según el representante de cada bloque.
     * - Estado inicial = bloque del estado inicial original.
     * - Estado de aceptación = bloque que contiene al menos un estado final.
     */
    private DFA buildMinimizedDFA(DFA original,
                                   List<Set<DFAState>> partitions,
                                   Set<Character> alphabet) {
        DFAState.resetCounter();
        Map<DFAState, Integer> blockIndex = buildBlockIndex(partitions);

        // Crear un estado nuevo por bloque y elegir un representante
        List<DFAState> newStateList  = new ArrayList<>();
        Map<Integer, DFAState> newStateByBlock = new LinkedHashMap<>();
        List<DFAState> representatives = new ArrayList<>();

        for (int i = 0; i < partitions.size(); i++) {
            Set<DFAState> block = partitions.get(i);
            DFAState rep = block.iterator().next();
            boolean isAccepting = block.stream().anyMatch(DFAState::isAccepting);

            DFAState newState = new DFAState(new TreeSet<>());
            newState.setAccepting(isAccepting);

            newStateByBlock.put(i, newState);
            newStateList.add(newState);
            representatives.add(rep);
        }

        // Conectar transiciones entre estados nuevos
        for (int i = 0; i < partitions.size(); i++) {
            DFAState rep      = representatives.get(i);
            DFAState newState = newStateByBlock.get(i);
            for (char sym : alphabet) {
                DFAState target = rep.getTransition(sym);
                if (target != null) {
                    int targetBlock = blockIndex.get(target);
                    newState.addTransition(sym, newStateByBlock.get(targetBlock));
                }
            }
        }

        // Estado inicial del AFD minimizado
        int initialBlock = blockIndex.get(original.getInitialState());
        DFAState newInitial = newStateByBlock.get(initialBlock);

        return new DFA(newInitial, newStateList, alphabet);
    }
}
