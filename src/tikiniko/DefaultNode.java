package tikiniko;

import breakthrough.Color;
import commons.Set;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created on 08/08/14.
 */
public class DefaultNode extends AbstractNode {

    private final Map<Color, Node> children = new EnumMap<>(Color.class);

    DefaultNode(Node blackChild, Node noneChild, Node whiteChild) {
        children.put(Color.Black, blackChild);
        children.put(Color.None, noneChild);
        children.put(Color.White, whiteChild);
    }

    @Override
    public Node union(Set<Node> node) {
        return null;
    }//todo

    @Override
    public Node intersection(Set<Node> set) {
        return null;
    }//todo

    @Override
    public Node complement() {
        return null;
    }//todo

    @Override
    public Node getChild(Color color) {
        return children.get(color);
    }
}
