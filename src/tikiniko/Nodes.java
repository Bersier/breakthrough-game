package tikiniko;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * <p>
 * Created on 8/9/2014.
 */
public class Nodes {

    /**
     * Used for hash-consing.
     */
    private static Map<Node, Node> memory = new WeakHashMap<>();

    public Node Node(Node blackChild, Node noneChild, Node whiteChild) {
        final Node node = new DefaultNode(blackChild, noneChild, whiteChild);
        final Node memNode = memory.putIfAbsent(node, node);
        return (memNode == null) ? node : memNode;
    }
}
