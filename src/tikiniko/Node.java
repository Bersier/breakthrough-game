package tikiniko;

import java.util.Collection;

import breakthrough.Color;
import commons.Set;

import static commons.Utils.List;

/**
 * Created on 08/08/14.
 *
 * @author stephanebersier
 */
public interface Node extends Set<Node> {

    Node getChild(Color color);

    default boolean hasChild(Color color) {
        return getChild(color) != null;
    }

    /**
     * @return all the children of this node
     */
    default Collection<Node> getChildren() {
        final Collection<Node> children = List();
        for(Color color : Color.values()) {
            final Node child = getChild(color);
            if(child != null) {
                children.add(child);
            }
        }
        return children;
    }
}
