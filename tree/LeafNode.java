import java.util.Set;

/** Leaf node in the regex AST representing a single symbol at a given position. */
public class LeafNode extends Node {

    /** The position number assigned to this leaf. */
    public int position;

    /** The symbol this leaf matches. */
    public char symbol;

    /** Returns true if this leaf represents the epsilon (empty) symbol. */
    @Override
    public boolean nullable() {
        // TODO: implement
        return false;
    }

    /** Returns the set of positions that can begin a match (just this position). */
    @Override
    public Set<Integer> firstPos() {
        // TODO: implement
        return null;
    }

    /** Returns the set of positions that can end a match (just this position). */
    @Override
    public Set<Integer> lastPos() {
        // TODO: implement
        return null;
    }

    /** Accepts a visitor and delegates to visitLeaf. */
    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        // TODO: implement
        return null;
    }
}
