package tree;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;
import visitor.NodeVisitor;
import java.util.Collections;
import java.util.Set;

/**
 * Hoja del árbol sintáctico.
 * Representa un símbolo del alfabeto (a, b, #) o epsilon.
 *
 * - Si symbol == 'ε', position es -1 y NO se cuenta como posición etiquetada.
 * - Cualquier otro símbolo tiene un número de posición único >= 1.
 */

public class LeafNode extends Node {

    public final char symbol;    // símbolo que representa ('a', 'b', '#', 'ε')
    public final int position;   // posición única; -1 si es epsilon

    public LeafNode(char symbol, int position) {
        this.symbol = symbol;
        this.position = position;
    }

    /**
     * Solo epsilon es nullable.
     */
    @Override
    public boolean nullable() {
        return symbol == 'ε';
    }

    /**
     * firstPos de una hoja:
     *   - ε     → conjunto vacío  (no aporta posición)
     *   - otro  → { position }
     */
    @Override
    public Set<Integer> firstPos() {
        if (symbol == 'ε') return Collections.emptySet();
        return Collections.singleton(position);
    }

    /**
     * lastPos de una hoja: idéntico a firstPos.
     */
    @Override
    public Set<Integer> lastPos() {
        return firstPos();
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitLeaf(this);
    }

    @Override
    public String toString() {
        return symbol == 'ε' ? "ε" : symbol + "(" + position + ")";
    }
}
