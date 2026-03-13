package visitor;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;
/** Visitor that computes the nullable property for each AST node. */
public class NullableVisitor implements NodeVisitor<Boolean> {

    /** Returns whether a leaf node is nullable. */
    @Override
    public Boolean visitLeaf(LeafNode node) {
        // A leaf is nullable only if it represents epsilon (position -1)
        return node.position == -1;
    }

    /** Returns whether a union node is nullable. */
    @Override
    public Boolean visitUnion(UnionNode node) {
        // nullable(c1|c2) = nullable(c1) OR nullable(c2)
        return node.left.accept(this) || node.right.accept(this);
    }

    /** Returns whether a concat node is nullable. */
    @Override
    public Boolean visitConcat(ConcatNode node) {
        // nullable(c1·c2) = nullable(c1) AND nullable(c2)
        return node.left.accept(this) && node.right.accept(this);
    }

    /** Returns whether a Kleene-star node is nullable. */
    @Override
    public Boolean visitKleene(KleeneNode node) {
        // c1* is always nullable (matches zero repetitions)
        return true;
    }
}
