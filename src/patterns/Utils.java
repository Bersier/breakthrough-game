package patterns;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import breakthrough.Color;
import patterns.node.Node;

/**
 * Created on 05/08/14.
 */
class Utils {

    static Set<Node> getChildren(Collection<Node> nodes, Color color) {
        return nodes.stream()
                .map(node -> node.getChild(color))
                .filter(child -> child != null)
                .collect(Collectors.toSet());
    }

    static Set<Node> getChildren(Collection<Node> nodes) {
        return getChildrenStream(nodes).collect(Collectors.toSet());
    }

    static Stream<Node> getChildrenStream(Collection<Node> nodes) {
        return nodes.stream()
                    .flatMap(node -> node.getChildren().stream())
                    .filter(child -> child != null);
    }

    static Map<Node, Node> makeMap(List<Node> list) {
        final Map<Node, Node> ans = new HashMap<>(list.size());
        for (Node node : list) {
            if (node == null) {
                throw new RuntimeException("Found null value in Node list!");
            }
            ans.put(node, node);
        }
        for (Node node : list) {
            if (node != null && ans.get(node) == null) {
                throw new RuntimeException("iiiiiiiiiiiiiiiiiiiiiiiiiiii");
            }
        }
        return ans;
    }

    static <A> Stack<A> copy(Stack<A> stack) {
        final Stack<A> ans = new Stack<A>();
        for (A a : stack) {
            ans.push(a);
        }
        return ans;
    }

    static Set<ColorList> copy(Set<ColorList> set) {
        final Set<ColorList> ans = new HashSet<ColorList>(set.size());
        for (ColorList stack : set) {
            ans.add(new ColorList(stack));
        }
        return ans;
    }

    static Set<List<Color>> trans(Set<ColorList> set) {
        final Set<List<Color>> ans = new HashSet<List<Color>>(set.size());
        for (ColorList stack : set) {
            ans.add(stack.toList());
        }
        return ans;
    }

    static Stack<Color> trans(List<Color> list) {
        final Stack<Color> ans = new Stack<>();
        final int size = list.size();
        for (int i = 1; i <= size; i++) {
            ans.push(list.get(size - i));
        }
        return ans;
    }

    static Set<Stack<Color>> transe(Set<List<Color>> set) {
        final Set<Stack<Color>> ans = new HashSet<>(set.size());
        for (List<Color> list : set) {
            ans.add(trans(list));
        }
        return ans;
    }
}
