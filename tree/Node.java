import visitor.NodeVisitor;
import java.util.Set;

/**
 * Clase base abstracta para todos los nodos del árbol sintáctico.
 */

public abstract class Node {

    /**
     * Calcula si este nodo puede generar la cadena vacía (epsilon).
     */
    public abstract boolean nullable();

    /**
     * Calcula el conjunto de posiciones que pueden ser el primer símbolo
     * de una cadena generada por este nodo.
     */
    public abstract Set<Integer> firstPos();

    /**
     * Calcula el conjunto de posiciones que pueden ser el último símbolo
     * de una cadena generada por este nodo.
     */
    public abstract Set<Integer> lastPos();

     /**
     * Acepta un visitante. Cada subclase llama al método visit correspondiente.
     * Patrón Visitor: double dispatch.
     */
    public abstract <T> T accept(NodeVisitor<T> visitor);
}
