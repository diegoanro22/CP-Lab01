package visitor;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;
import java.util.Set;
import java.util.HashSet;

/** Visitor that computes the firstPos set for each AST node. */
public class FirstPosVisitor implements NodeVisitor<Set<Integer>> {

    private NullableVisitor nullableVisitor = new NullableVisitor();

    /** Returns the firstPos set for a leaf node. */
    @Override
    public Set<Integer> visitLeaf(LeafNode node) {
        // firstPos of a leaf is {position} if not epsilon, else empty set
        Set<Integer> result = new HashSet<>();
        if (node.position != -1) {
            result.add(node.position);
        }
        return result;
    }

    /** Returns the firstPos set for a union node. */
    @Override
    public Set<Integer> visitUnion(UnionNode node) {
        // firstPos(c1|c2) = firstPos(c1) ∪ firstPos(c2)
        Set<Integer> result = new HashSet<>(node.left.accept(this));
        result.addAll(node.right.accept(this));
        return result;
    }

    /** Returns the firstPos set for a concat node. */
    @Override
    public Set<Integer> visitConcat(ConcatNode node) {
        // If left is nullable: firstPos(c1·c2) = firstPos(c1) ∪ firstPos(c2)
        // Otherwise: firstPos(c1·c2) = firstPos(c1)
        Set<Integer> result = new HashSet<>(node.left.accept(this));
        if (node.left.accept(nullableVisitor)) {
            result.addAll(node.right.accept(this));
        }
        return result;
    }

    /** Returns the firstPos set for a Kleene-star node. */
    @Override
    public Set<Integer> visitKleene(KleeneNode node) {
        // firstPos(c1*) = firstPos(c1)
        return new HashSet<>(node.child.accept(this));
    }
}
