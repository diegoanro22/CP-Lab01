import java.util.Set;

/** Visitor that computes the lastPos set for each AST node. */
public class LastPosVisitor implements NodeVisitor<Set<Integer>> {

    /** Returns the lastPos set for a leaf node. */
    @Override
    public Set<Integer> visitLeaf(LeafNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the lastPos set for a union node. */
    @Override
    public Set<Integer> visitUnion(UnionNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the lastPos set for a concat node. */
    @Override
    public Set<Integer> visitConcat(ConcatNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the lastPos set for a Kleene-star node. */
    @Override
    public Set<Integer> visitKleene(KleeneNode node) {
        // TODO: implement
        return null;
    }
}
