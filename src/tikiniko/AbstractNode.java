package tikiniko;

import breakthrough.Color;
import commons.Utils;

import java.util.Arrays;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Stream;

/**
 * <p>
 * Created on 8/9/2014.
 */
abstract class AbstractNode implements Node {

    @Override
    public boolean hasChild(Color color) {
        return getChild(color) != null;
    }

    @Override
    public Stream<Node> getChildren() {
        return Arrays.stream(Color.values()).filter(this::hasChild).map(this::getChild);
    }

    /**
     * As nodes expect to be stored in a weak hash map, there should be at most one duplicate node.
     * Therefore, to check equality, it suffices to check that all children are identical.
     */
    @Override
    public boolean equals(Object otherNode) {
        final Node other = (Node) otherNode;
        for (Color color : Color.values()) {
            if (getChild(color) != getChild(color)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Matches the equals method above.
     */
    @Override
    public int hashCode() {
        int ans = 0;
        int i = 0;
        for (Color color : Color.values()) {
            final AbstractNode child = (AbstractNode) getChild(color);
            ans ^= Utils.rotl((child == null) ? NULL_NODE_HASH : child.memoryAddress(), i);
            i++;
        }
        return ans;
    }

    /**
     * @return the original hashCode, which should be a simple variation on the memory address
     */
    private int memoryAddress() {
        return super.hashCode();
    }
}
