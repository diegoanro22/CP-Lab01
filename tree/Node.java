import java.util.Set;

/** Abstract base class representing a node in the regex AST. */
public abstract class Node {

    /** Returns true if this node's language contains the empty string. */
    public abstract boolean nullable();

    /** Returns the set of positions that can begin a string matched by this node. */
    public abstract Set<Integer> firstPos();

    /** Returns the set of positions that can end a string matched by this node. */
    public abstract Set<Integer> lastPos();

    /** Accepts a visitor and returns its computed result. */
    public abstract <T> T accept(NodeVisitor<T> visitor);
}
