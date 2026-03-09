import java.util.Set;

/** AST node representing the concatenation of two sub-expressions. */
public class ConcatNode extends Node {

    /** The left operand of the concatenation. */
    public Node left;

    /** The right operand of the concatenation. */
    public Node right;

    /** Returns true if both children are nullable. */
    @Override
    public boolean nullable() {
        // TODO: implement
        return false;
    }

    /** Returns firstPos of left, union firstPos of right if left is nullable. */
    @Override
    public Set<Integer> firstPos() {
        // TODO: implement
        return null;
    }

    /** Returns lastPos of right, union lastPos of left if right is nullable. */
    @Override
    public Set<Integer> lastPos() {
        // TODO: implement
        return null;
    }

    /** Accepts a visitor and delegates to visitConcat. */
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        // TODO: implement
        return null;
    }
}
