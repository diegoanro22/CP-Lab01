import java.util.Set;

/** AST node representing the union (alternation) of two sub-expressions. */
public class UnionNode extends Node {

    /** The left operand of the union. */
    public Node left;

    /** The right operand of the union. */
    public Node right;

    /** Returns true if either child is nullable. */
    @Override
    public boolean nullable() {
        // TODO: implement
        return false;
    }

    /** Returns the union of firstPos sets of both children. */
    @Override
    public Set<Integer> firstPos() {
        // TODO: implement
        return null;
    }

    /** Returns the union of lastPos sets of both children. */
    @Override
    public Set<Integer> lastPos() {
        // TODO: implement
        return null;
    }

    /** Accepts a visitor and delegates to visitUnion. */
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        // TODO: implement
        return null;
    }
}
