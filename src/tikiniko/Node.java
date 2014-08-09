package tikiniko;

import breakthrough.Color;
import commons.Set;

import java.util.Random;
import java.util.stream.Stream;

/**
 * All implementation of this class should extend AbstractNode.
 *
 * Created on 08/08/14.
 */
public interface Node extends Set<Node> {

    static final int NULL_NODE_HASH = (new Random()).nextInt();

    /**
     * @return the child of this node of the given color if it exists, null otherwise
     */
    Node getChild(Color color);

    /**
     * @return whether this node has a child of the given color
     */
    boolean hasChild(Color color);

    /**
     * @return all the children of this node
     */
    Stream<Node> getChildren();

    /**
     * @param otherNode this or another node
     * @return whether the two nodes are equivalent
     */
    @Override
    boolean equals(Object otherNode);

    @Override
    int hashCode();
}
