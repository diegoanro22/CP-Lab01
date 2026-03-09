import java.util.Map;
import java.util.Set;

/** Visitor that computes the followPos sets by traversing the AST. */
public class FollowPosVisitor implements NodeVisitor<Void> {

    /** Maps each position to the set of positions that can follow it. */
    private Map<Integer, Set<Integer>> followPos;

    /** Returns null after updating followPos for a leaf node. */
    @Override
    public Void visitLeaf(LeafNode node) {
        // TODO: implement
        return null;
    }

    /** Returns null after updating followPos for a union node. */
    @Override
    public Void visitUnion(UnionNode node) {
        // TODO: implement
        return null;
    }

    /** Returns null after updating followPos for a concat node. */
    @Override
    public Void visitConcat(ConcatNode node) {
        // TODO: implement
        return null;
    }

    /** Returns null after updating followPos for a Kleene-star node. */
    @Override
    public Void visitKleene(KleeneNode node) {
        // TODO: implement
        return null;
    }

    /** Returns the computed followPos map. */
    public Map<Integer, Set<Integer>> getFollowPos() {
        // TODO: implement
        return null;
    }
}
