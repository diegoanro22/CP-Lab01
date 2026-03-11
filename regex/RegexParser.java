/**
 * Parser de expresiones regulares usando descenso recursivo.
 *
 * Recibe la expresión ya preprocesada (con · explícito, sin +, sin ?)
 * y construye el árbol sintáctico usando NodeFactory.
 *
 * Gramática que implementa (de menor a mayor precedencia):
 *
 *   expr   →  term  ( '|' term )*          // unión, menor precedencia
 *   term   →  factor ( '·' factor )*        // concatenación
 *   factor →  atom ( '*' )*                 // Kleene, mayor precedencia
 *   atom   →  SYMBOL | 'ε' | '(' expr ')'  // base
 *
 */

public class RegexParser {

    private final String input;    // expresión regular preprocesada (con · explícito, sin +, sin ?)
    private int pos;                // posición actual en el input
    private int posCounter;     // contador de posición para asignar a los nodos

    public RegexParser(String input) {
        this.input = input;
        this.pos = 0;
        this.posCounter = 0;
    }

    /** Función de entrada para iniciar el parsing. */
    public Node parse() {
        Node result = expr();
        if (pos != input.length()) {
            throw new RuntimeException("Error de sintaxis: token inesperado en posición " + pos);
        }
        return result;
    }


    /**
     * expr → term ( '|' term )*
     *
     * Parsea una o más alternativas separadas por |.
     * Si solo hay una, retorna el term directamente (sin UnionNode).
     *
     * Ejemplo: a|b|c → Union(a, Union(b, c))  [asocia a la derecha]
     */
    private Node expr() {
        Node left = term();

        while (pos < input.length() && current() == '|') {
            consume('|');
            Node right = term();
            left = NodeFactory.union(left, right);
        }

        return left;
    }


    /**
     * term → factor ( '·' factor )*
     *
     * Parsea una o más concatenaciones separadas por ·.
     * Si solo hay una, retorna el factor directamente (sin ConcatNode).
     *
     * Ejemplo: a·b·c → Concat(a, Concat(b, c))  [asocia a la derecha]
     */

    private Node term() {
        Node left = factor();

        while (pos < input.length() && current() == '·') {
            consume('·');
            Node right = factor();
            left = NodeFactory.concat(left, right);
        }

        return left;
    }


    /**
     * factor → atom ( '*' )*
     *
     * Parsea un atom seguido de cero o más *.
     * Si hay uno o más *, envuelve el atom en un StarNode.
     *
     * Ejemplo: a* → Star(a), (a|b)* → Star(Union(a, b))
     */

    private Node factor() {
        Node base = atom();

        while (pos < input.length() && current() == '*') {
            consume('*');
            base = NodeFactory.kleene(base);
        }

        return base;


    private Node atom() {
        if (pos >= input.length()) {
            throw new IllegalArgumentException("Se esperaba un símbolo pero se llegó al final.");
        }

        char c = current();

        if (c == '(') {
            // Grupo entre paréntesis: parsea el interior como expr completa
            consume('(');
            Node node = expr();
            consume(')');
            return node;
        }

        if (c == 'ε') {
            consume('ε');
            return NodeFactory.epsilon();
        }

        // Cualquier otro carácter es un símbolo del alfabeto
        if (c == '|' || c == '*' || c == '·' || c == ')') {
            throw new IllegalArgumentException(
                "Se esperaba un símbolo pero se encontró operador '" + c + "' en posición " + pos
            );
        }

        consume(c);
        posCounter++;
        return NodeFactory.leaf(c, posCounter);
    }


    /** Retorna el carácter actual sin consumirlo. */
    private char current() {
        return input.charAt(pos);
    }   

    /** Consume el carácter esperado, avanzando la posición. */
    private void consume(char expected) {
        if (pos >= input.length()) {
            throw new IllegalArgumentException(
                "Se esperaba '" + expected + "' pero se llegó al final de la expresión."
            );
        }
        if (input.charAt(pos) != expected) {
            throw new IllegalArgumentException(
                "Se esperaba '" + expected + "' pero se encontró '" + input.charAt(pos) +
                "' en posición " + pos
            );
        }
        pos++;
    }

    /** Retorna la cantidad de posiciones asignadas a los nodos hoja */
    public int getPosCounter() {
        return posCounter;
    }
}
