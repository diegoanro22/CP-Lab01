/** Visitor that computes the nullable property for each AST node. */
public class NullableVisitor implements NodeVisitor<Boolean> {

    /** Returns whether a leaf node is nullable. */
    @Override
    public Boolean visitLeaf(LeafNode node) {
        // TODO: implement
        return null;
    }

    /** Returns whether a union node is nullable. */
    @Override
    public Boolean visitUnion(UnionNode node) {
        // TODO: implement
        return null;
    }

    /** Returns whether a concat node is nullable. */
    @Override
    public Boolean visitConcat(ConcatNode node) {
        // TODO: implement
        return null;
    }

    /** Returns whether a Kleene-star node is nullable. */
    @Override
    public Boolean visitKleene(KleeneNode node) {
        // TODO: implement
        return null;
    }
}
