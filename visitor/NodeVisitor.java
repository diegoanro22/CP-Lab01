package visitor;

import dfa.*;
import regex.*;
import simulation.*;
import tree.*;
import visitor.*;
/** Generic visitor interface for traversing regex AST nodes. */
public interface NodeVisitor<T> {

    /** Visits a leaf node and returns a result of type T. */
    T visitLeaf(LeafNode node);

    /** Visits a union node and returns a result of type T. */
    T visitUnion(UnionNode node);

    /** Visits a concatenation node and returns a result of type T. */
    T visitConcat(ConcatNode node);

    /** Visits a Kleene-star node and returns a result of type T. */
    T visitKleene(KleeneNode node);
}
