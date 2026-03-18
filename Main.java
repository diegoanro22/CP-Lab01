import dfa.DFA;
import dfa.DFABuilder;
import dfa.DFAMinimizer;
import regex.RegexParser;
import regex.RegexPreprocessor;
import simulation.DFASimulator;
import tree.Node;
import visitor.FirstPosVisitor;
import visitor.FollowPosVisitor;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printBanner();

        while (true) {
            // 1. Leer expresion regular
            System.out.print("Ingresa la expresion regular ('salir' para terminar): ");
            String rawRegex = scanner.nextLine().trim();

            if (rawRegex.equalsIgnoreCase("salir")) {
                System.out.println("Hasta luego!");
                break;
            }
            if (rawRegex.isEmpty()) continue;

            try {
                // 2. Preprocesar
                // RegexPreprocessor ya agrega el '#' al final internamente
                RegexPreprocessor preprocessor = new RegexPreprocessor();
                String processedRegex = preprocessor.preprocess(rawRegex);
                System.out.println("  Expresion aumentada: " + processedRegex + "\n");

                // 3. Parsear y construir arbol
                RegexParser parser = new RegexParser(processedRegex);
                Node root = parser.parse();

                // 4. Visitors
                // FirstPosVisitor implementa NodeVisitor<Set<Integer>>: devuelve el resultado via accept()
                Set<Integer> initialPositions = root.accept(new FirstPosVisitor());

                // FollowPosVisitor implementa NodeVisitor<Void>: los resultados se guardan internamente
                FollowPosVisitor followVisitor = new FollowPosVisitor();
                root.accept(followVisitor);
                Map<Integer, Set<Integer>> followPos     = followVisitor.getFollowPos();
                Map<Integer, Character>    positionSymbols = followVisitor.getPositionSymbols();

                // La posicion de '#' es la que tiene el simbolo '#' en positionSymbols
                int hashPosition = positionSymbols.entrySet().stream()
                        .filter(e -> e.getValue() == '#')
                        .mapToInt(Map.Entry::getKey)
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("No se encontro la posicion de '#'"));

                // 5. Construir AFD directo
                DFABuilder builder = new DFABuilder(followPos, positionSymbols,
                        initialPositions, hashPosition);
                DFA directDFA = builder.build();

                // 6. Mostrar tabla AFD directo
                directDFA.printSummary("AFD Directo");

                // 7. Minimizar
                DFAMinimizer minimizer = new DFAMinimizer();
                DFA minDFA = minimizer.minimize(directDFA);

                // 8. Mostrar tabla AFD minimizado
                minDFA.printSummary("AFD Minimizado");

                // 9. Comparacion
                printComparison(directDFA, minDFA);

                // 10. Simulacion con AFD minimizado
                DFASimulator simulator = new DFASimulator(minDFA);

                while (true) {
                    System.out.print("Cadena a evaluar ('nueva' para otra regex, 'salir' para terminar): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("salir")) {
                        System.out.println("Hasta luego!");
                        scanner.close();
                        return;
                    }
                    if (input.equalsIgnoreCase("nueva")) break;

                    boolean accepted = simulator.simulate(input);
                    System.out.println("  Resultado: " + (accepted ? "ACEPTADO" : "RECHAZADO") + "\n");
                }

            } catch (Exception e) {
                System.out.println("  [Error] " + e.getMessage());
                System.out.println("  Verifica la sintaxis de la expresion e intenta de nuevo.\n");
            }
        }

        scanner.close();
    }

    private static void printBanner() {
        System.out.println("============================================");
        System.out.println("   Regex -> AFD Directo + Minimizacion");
        System.out.println("   Diseno de Lenguajes de Programacion 2026");
        System.out.println("============================================");
        System.out.println("Operadores: | * + ?");
        System.out.println();
    }

    private static void printComparison(DFA direct, DFA min) {
        int statesBefore = direct.countStates();
        int statesAfter  = min.countStates();
        int transBefore  = direct.countTransitions();
        int transAfter   = min.countTransitions();
        int stateReduced = statesBefore - statesAfter;
        int transReduced = transBefore  - transAfter;

        System.out.println("--- Comparacion AFD Directo vs Minimizado ---");
        System.out.printf("  Estados      : %d -> %d  (%s)%n",
                statesBefore, statesAfter, delta(stateReduced));
        System.out.printf("  Transiciones : %d -> %d  (%s)%n",
                transBefore, transAfter, delta(transReduced));

        if (stateReduced == 0) {
            System.out.println("  El AFD ya era minimo, no se redujo.");
        } else {
            System.out.println("  Se eliminaron " + stateReduced + " estado(s) equivalente(s).");
        }
        System.out.println();
    }

    private static String delta(int d) {
        if (d > 0) return "-" + d;
        if (d < 0) return "+" + (-d) + " (aumento)";
        return "sin cambio";
    }
}