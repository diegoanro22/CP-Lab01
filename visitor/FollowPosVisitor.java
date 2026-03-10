import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

/** Visitor that computes the followPos sets by traversing the AST. */
public class FollowPosVisitor implements NodeVisitor<Void> {

    /** Maps each position to the set of positions that can follow it. */
    private Map<Integer, Set<Integer>> followPos;
    
    /** Maps each position to its symbol (for later reference). */
    private Map<Integer, Character> positionSymbols;

    /** Constructor initializes the followPos map. */
    public FollowPosVisitor() {
        this.followPos = new HashMap<>();
        this.positionSymbols = new HashMap<>();
    }

    /** Returns null after updating followPos for a leaf node. */
    @Override
    public Void visitLeaf(LeafNode node) {
        // Leaf nodes don't generate followPos entries, but record symbol for this position
        if (node.position != -1) {
            positionSymbols.put(node.position, node.symbol);
            // Initialize followPos if not already created
            followPos.putIfAbsent(node.position, new HashSet<>());
        }
        return null;
    }

    /** Returns null after updating followPos for a union node. */
    @Override
    public Void visitUnion(UnionNode node) {
        // Process both children recursively
        node.left.accept(this);
        node.right.accept(this);
        return null;
    }

    /** Returns null after updating followPos for a concat node. */
    @Override
    public Void visitConcat(ConcatNode node) {
        // For concatenation: add firstPos(right) to followPos(each position in lastPos(left))
        LastPosVisitor lastPosVisitor = new LastPosVisitor();
        FirstPosVisitor firstPosVisitor = new FirstPosVisitor();
        
        Set<Integer> lastPosLeft = node.left.accept(lastPosVisitor);
        Set<Integer> firstPosRight = node.right.accept(firstPosVisitor);
        
        for (int pos : lastPosLeft) {
            followPos.putIfAbsent(pos, new HashSet<>());
            followPos.get(pos).addAll(firstPosRight);
        }
        
        // Recursively process children
        node.left.accept(this);
        node.right.accept(this);
        return null;
    }

    /** Returns null after updating followPos for a Kleene-star node. */
    @Override
    public Void visitKleene(KleeneNode node) {
        // For Kleene star: add firstPos(child) to followPos(each position in lastPos(child))
        LastPosVisitor lastPosVisitor = new LastPosVisitor();
        FirstPosVisitor firstPosVisitor = new FirstPosVisitor();
        
        Set<Integer> lastPosChild = node.child.accept(lastPosVisitor);
        Set<Integer> firstPosChild = node.child.accept(firstPosVisitor);
        
        for (int pos : lastPosChild) {
            followPos.putIfAbsent(pos, new HashSet<>());
            followPos.get(pos).addAll(firstPosChild);
        }
        
        // Recursively process child
        node.child.accept(this);
        return null;
    }

    /** Returns the computed followPos map. */
    public Map<Integer, Set<Integer>> getFollowPos() {
        return followPos;
    }

    /** Returns the position to symbol mapping. */
    public Map<Integer, Character> getPositionSymbols() {
        return positionSymbols;
    }
}
