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
 * Nodo interno que representa el operador unión: left | right
 */
public class UnionNode extends Node {

    public final Node left;
    public final Node right;

    public UnionNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    /**
     * c1 | c2  →  nullable si alguno de los dos es nullable.
     */
    @Override
    public boolean nullable() {
        return left.nullable() || right.nullable();
    }

    /**
     * firstPos(c1|c2) = firstPos(c1) ∪ firstPos(c2)
     */
    @Override
    public Set<Integer> firstPos() {
        Set<Integer> result = new HashSet<>(left.firstPos());
        result.addAll(right.firstPos());
        return result;
    }

    /**
     * lastPos(c1|c2) = lastPos(c1) ∪ lastPos(c2)
     */
    @Override
    public Set<Integer> lastPos() {
        Set<Integer> result = new HashSet<>(left.lastPos());
        result.addAll(right.lastPos());
        return result;
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitUnion(this);
    }

    @Override
    public String toString() {
        return "(" + left + "|" + right + ")";
    }
}
