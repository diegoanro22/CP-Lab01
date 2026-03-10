import java.util.Set;
import java.util.HashSet;

/** Visitor that computes the lastPos set for each AST node. */
public class LastPosVisitor implements NodeVisitor<Set<Integer>> {

    private NullableVisitor nullableVisitor = new NullableVisitor();

    /** Returns the lastPos set for a leaf node. */
    @Override
    public Set<Integer> visitLeaf(LeafNode node) {
        // lastPos of a leaf is {position} if not epsilon, else empty set
        Set<Integer> result = new HashSet<>();
        if (node.position != -1) {
            result.add(node.position);
        }
        return result;
    }

    /** Returns the lastPos set for a union node. */
    @Override
    public Set<Integer> visitUnion(UnionNode node) {
        // lastPos(c1|c2) = lastPos(c1) ∪ lastPos(c2)
        Set<Integer> result = new HashSet<>(node.left.accept(this));
        result.addAll(node.right.accept(this));
        return result;
    }

    /** Returns the lastPos set for a concat node. */
    @Override
    public Set<Integer> visitConcat(ConcatNode node) {
        // If right is nullable: lastPos(c1·c2) = lastPos(c1) ∪ lastPos(c2)
        // Otherwise: lastPos(c1·c2) = lastPos(c2)
        Set<Integer> result = new HashSet<>(node.right.accept(this));
        if (node.right.accept(nullableVisitor)) {
            result.addAll(node.left.accept(this));
        }
        return result;
    }

    /** Returns the lastPos set for a Kleene-star node. */
    @Override
    public Set<Integer> visitKleene(KleeneNode node) {
        // lastPos(c1*) = lastPos(c1)
        return new HashSet<>(node.child.accept(this));
    }
}
