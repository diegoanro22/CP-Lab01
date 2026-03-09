import java.util.Set;

/** AST node representing the Kleene star (zero-or-more repetition) of a sub-expression. */
public class KleeneNode extends Node {

    /** The child sub-expression being repeated. */
    public Node child;

    /** Always returns true because Kleene star allows zero repetitions. */
    @Override
    public boolean nullable() {
        // TODO: implement
        return false;
    }

    /** Returns the firstPos set of the child. */
    @Override
    public Set<Integer> firstPos() {
        // TODO: implement
        return null;
    }

    /** Returns the lastPos set of the child. */
    @Override
    public Set<Integer> lastPos() {
        // TODO: implement
        return null;
    }

    /** Accepts a visitor and delegates to visitKleene. */
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        // TODO: implement
        return null;
    }
}
