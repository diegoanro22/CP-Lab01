/**
 * Patrón Factory: punto central para crear todos los nodos del árbol.
 */

public class NodeFactory {

    // Constructor privado: esta clase no se instancia, solo tiene métodos estáticos.
    private NodeFactory() {}

    /** Crea un nodo de unión: left | right */
    public static Node union(Node left, Node right) {
        return new UnionNode(left, right);
    }

    /** Crea un nodo de concatenación: left · right */
    public static Node concat(Node left, Node right) {
        return new ConcatNode(left, right);
    }

    /** Crea un nodo de estrella de Kleene: child* */
    public static Node kleene(Node child) {
        return new KleeneNode(child);
    }

    /**
     * Crea una hoja con símbolo y posición.
     * @param symbol   carácter del alfabeto (ej. 'a', 'b', '#')
     * @param position número de etiqueta único (>= 1)
     */
    public static Node leaf(char symbol, int position) {
        return new LeafNode(symbol, position);
    }

    /** Crea un nodo hoja que representa el epsilon (cadena vacía). */
    public static Node epsilon() {
        return new LeafNode('ε', -1);
    }
}
