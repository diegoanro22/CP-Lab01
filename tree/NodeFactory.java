/** Factory for creating AST node instances. */
public class NodeFactory {

    /** Creates a UnionNode with the given left and right children. */
    public static Node union(Node left, Node right) {
        // TODO: implement
        return null;
    }

    /** Creates a ConcatNode with the given left and right children. */
    public static Node concat(Node left, Node right) {
        // TODO: implement
        return null;
    }

    /** Creates a KleeneNode wrapping the given child node. */
    public static Node kleene(Node child) {
        // TODO: implement
        return null;
    }

    /** Creates a LeafNode for the given symbol at the given position. */
    public static Node leaf(int position, char symbol) {
        // TODO: implement
        return null;
    }

    /** Creates a LeafNode representing the epsilon (empty string). */
    public static Node epsilon() {
        // TODO: implement
        return null;
    }
}
