import java.util.Set;

/** Visitor that computes the firstPos set for each AST node. */
public class FirstPosVisitor implements NodeVisitor<Set<Integer>> {

    /** Returns the firstPos set for a leaf node. */
    @Override
    public Set<Integer> visitLeaf(LeafNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the firstPos set for a union node. */
    @Override
    public Set<Integer> visitUnion(UnionNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the firstPos set for a concat node. */
    @Override
    public Set<Integer> visitConcat(ConcatNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the firstPos set for a Kleene-star node. */
    @Override
    public Set<Integer> visitKleene(KleeneNode node) {
        // TODO: implement
        return null;
    }
}
