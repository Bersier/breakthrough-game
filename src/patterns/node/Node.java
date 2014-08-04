package patterns.node;

import breakthrough.Color;

import java.util.ArrayList;
import java.util.Collection;

// add defs to make it WrapperNode-equiv?

/**
 * Vertex of a [dag representing the [set of boards where white can win in N moves]].
 *
 * Each Node can have at most one edge of each {@link breakthrough.Color}.
 * So each edge is labelled by a color.
 *
 * Each node represents a set, which is recursively defined by the sets defined by its
 * children as follows:
 * The set represented by its black child is intersected with the set of all boards that
 * have a black pawn at the depth of this node.
 * Similarly for none and white children.
 * This node represents the union of the sets.
 * Just another way to say that it's a finite-state machine.
 */
public interface Node {

    /**
     * @return the node obtained by following the edge of the given color if it exists,
     * null otherwise
     */
	Node getChild(Color color);

    /**
     * @return all the children of this node
     */
    default Collection<Node> getChildren() {
        final Collection<Node> children = new ArrayList<>(1);
        for(Color color : Color.values()) {
            final Node child = getChild(color);
            if(child != null) {
                children.add(child);
            }
        }
        return children;
    }

    /**
     * Add an edge of the given color between this node and the given node. If there is already
     * an edge of that color from this node, it is replaced (the old edge is removed).
     */
	void setChild(Color color, Node node);

    /**
     * Mark this node as not visited.
     */
	void unvisit();

    /**
     * Seems to perform a three-way merge, prioritizing the two given nodes...
     */
	void merge(Node node1, Node node2);//todo seems unused...

    /**
     * @return super.hashCode()
     */
	int oldHash();
	
	//boolean isLeaf();

    /**
     * Only supported by WrapperNode.
     *
     * @return the node wrapped by this
     */
	Node getNode();

    /**
     * Unsupported by INode.
     *
     * @param node
     * @return whether the given node is the same object,
     * or contains the same object, in the case of a wrapper
     */
	boolean isSame(Node node);

    /**
     * Unsupported by LittleNode
     *
     * Get rid of wrappers, and maybe more...
     */
	void solidify();

    /**
     * Only supported by WrapperNode. Replace the wrapped node by the given node.
     */
	void become(Node node);

    /**
     * Use to print out the graph in the [graph description language] DOT.
     */
	void print();
}
