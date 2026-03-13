import dfa.DFA;
import dfa.DFABuilder;
import regex.RegexParser;
import regex.RegexPreprocessor;
import simulation.DFASimulator;
import tree.Node;
import visitor.FollowPosVisitor;
import visitor.FirstPosVisitor;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("   Conversión Regex → AFD (método directo)");
        System.out.println("========================================");
        System.out.println("Operadores soportados: | · * + ?");
        System.out.println("Escribe 'salir' para terminar.\n");

        while (true) {
            // ── 1. Leer expresión regular ──────────────────────────────────────
            System.out.print("Ingresa la expresión regular: ");
            String rawRegex = scanner.nextLine().trim();

            if (rawRegex.equalsIgnoreCase("salir")) {
                System.out.println("¡Hasta luego!");
                break;
            }
            if (rawRegex.isEmpty()) continue;

            try {
                // ── 2. Preprocesar: expandir +, ?, insertar ·, agregar # ──────
                RegexPreprocessor preprocessor = new RegexPreprocessor();
                String processedRegex = preprocessor.preprocess(rawRegex);
                System.out.println("  Expresión aumentada: " + processedRegex);

                // ── 3. Parsear y construir árbol ───────────────────────────────
                RegexParser parser = new RegexParser(processedRegex);
                Node root = parser.parse();

                // ── 4. Calcular firstPos de la raíz (estado inicial del AFD) ──
                FirstPosVisitor fpv = new FirstPosVisitor();
                Set<Integer> initialPositions = root.accept(fpv);

                // ── 5. Calcular followPos y mapa posición→símbolo ─────────────
                FollowPosVisitor followVisitor = new FollowPosVisitor();
                root.accept(followVisitor);
                Map<Integer, Set<Integer>> followPos = followVisitor.getFollowPos();
                Map<Integer, Character> positionSymbols = followVisitor.getPositionSymbols();

                // Obtener la posición asignada a '#'
                int hashPosition = parser.getPosCounter();

                // ── 6. Construir el AFD ────────────────────────────────────────
                DFABuilder builder = new DFABuilder(followPos, positionSymbols,
                        initialPositions, hashPosition);
                DFA dfa = builder.build();

                // ── 7. Mostrar tabla de transición ────────────────────────────
                System.out.println("\n--- Tabla de transición del AFD ---");
                dfa.printTable();

                // ── 8. Ciclo de simulación ─────────────────────────────────────
                DFASimulator simulator = new DFASimulator(dfa);

                while (true) {
                    System.out.print("\nIngresa una cadena a evaluar (o 'nueva' para otra regex, 'salir' para terminar): ");
                    String input = scanner.nextLine().trim();

                    if (input.equalsIgnoreCase("salir")) {
                        System.out.println("¡Hasta luego!");
                        scanner.close();
                        return;
                    }
                    if (input.equalsIgnoreCase("nueva")) break;

                    boolean accepted = simulator.simulate(input);
                    System.out.println("  Resultado: " + (accepted ? "✔ ACEPTADO" : "✘ RECHAZADO"));
                }

            } catch (Exception e) {
                System.out.println("  [Error] " + e.getMessage());
                System.out.println("  Verifica la sintaxis de la expresión e intenta de nuevo.");
            }

            System.out.println();
        }

        scanner.close();
    }
}
