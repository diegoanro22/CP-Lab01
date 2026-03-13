package tree;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;
import visitor.NodeVisitor;
import java.util.HashSet;
import java.util.Set;

/**
 * Nodo interno que representa la concatenación: left · right
 */
public class ConcatNode extends Node {

    public final Node left;
    public final Node right;

    public ConcatNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    /**
     * c1·c2  →  nullable solo si AMBOS son nullable.
     */
    @Override
    public boolean nullable() {
        return left.nullable() && right.nullable();
    }

    /**
     * firstPos(c1·c2):
     *   - si c1 es nullable → firstPos(c1) ∪ firstPos(c2)
     *   - si no             → firstPos(c1)
     */
    @Override
    public Set<Integer> firstPos() {
        if (left.nullable()) {
            Set<Integer> result = new HashSet<>(left.firstPos());
            result.addAll(right.firstPos());
            return result;
        }
        return new HashSet<>(left.firstPos());
    }

    /**
     * lastPos(c1·c2):
     *   - si c2 es nullable → lastPos(c1) ∪ lastPos(c2)
     *   - si no             → lastPos(c2)
     */
    @Override
    public Set<Integer> lastPos() {
        if (right.nullable()) {
            Set<Integer> result = new HashSet<>(left.lastPos());
            result.addAll(right.lastPos());
            return result;
        }
        return new HashSet<>(right.lastPos());
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitConcat(this);
    }

    @Override
    public String toString() {
        return "(" + left + "·" + right + ")";
    }
}
