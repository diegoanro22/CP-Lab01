import visitor.NodeVisitor;
import java.util.HashSet;
import java.util.Set;

/** AST node representing the Kleene star (zero-or-more repetition) of a sub-expression. */
public class KleeneNode extends Node {

    public final Node child;

    public KleeneNode(Node child) {
        this.child = child;
    }

     /**
     * c* siempre es nullable (puede generar la cadena vacía).
     */
    @Override
    public boolean nullable() {
        return true;
    }

    /**
     * firstPos(c*) = firstPos(c)
     */
    @Override
    public Set<Integer> firstPos() {
        return new HashSet<>(child.firstPos());
    }

    /**
     * lastPos(c*) = lastPos(c)
     */
    @Override
    public Set<Integer> lastPos() {
        return new HashSet<>(child.lastPos());
    }

    @Override
    public <T> T accept(NodeVisitor<T> visitor) {
        return visitor.visitKleene(this);
    }

    @Override
    public String toString() {
        return "(" + child + ")*";
    }
