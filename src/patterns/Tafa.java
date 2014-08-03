package patterns;

import breakthrough.Color;
import breakthrough.game.ASCIIBoardViewer;
import patterns.node.INode;
import patterns.node.LittleNode;
import patterns.node.Node;

import java.util.*;
import java.util.stream.Collectors;

public final class Tafa extends INode {// replace ArrayLists by Stacks?

    /**
     * The length of the dag (?)
     */
    private int length;
    private final int size;

    public Tafa(int size, int length) {
        this.length = length;
        this.size = size;
    }

    int getDepth() {
        return length / size;
    }

    int getSize() {
        return size;
    }

    int getLength() {
        return length;
    }

    /**
     * @return the number of nodes reachable from this one
     */
    int graphSize() {
        int count = 0;

        // keeps the nodes reachable from this node in 0, 1, 2, ... steps
        Set<Node> current = new HashSet<>(1);
        {
            current.add(this);
        }

        // keep going as long as possible
        while (!current.isEmpty()) {
            count += current.size();

            // store nodes reachable from current in one step in next
            final Set<Node> next = new HashSet<>(current.size());
            for (Node node : current) {
                next.addAll(node.getChildren());
            }

            current = next;
        }
        return count;
    }

    public void print() {
        Map<Node, Node> current = new IdentityHashMap<Node, Node>(3);
        for (Color color : Color.values()) {
            final Node child = getChild(color);
            if (child != null) {
                current.put(child, child);
            }
        }
        System.out.println("digraph Tafa {");
        super.print();
        while (!current.isEmpty()) {
            final Map<Node, Node> next =
                    new IdentityHashMap<Node, Node>(current.size());
            for (Node node : current.keySet()) {
                node.print();
                for (Color color : Color.values()) {
                    final Node child = node.getChild(color);
                    if (child != null) {
                        next.put(child, child);
                    }
                }
            }
            current = next;
        }
        System.out.println("}");
    }

    List<Node> getLayer(int depth) {
        Set<Node> current = new HashSet<Node>(1);
        current.add(this);
        for (int i = 0; i < depth; i++) {
            final Set<Node> next =
                    new HashSet<Node>(current.size());
            for (Node node : current) {
                for (Color color : Color.values()) {
                    final Node child = node.getChild(color);
                    if (child != null) {
                        next.add(child);
                    }
                }
            }
            current = next;
        }
        return new ArrayList<Node>(current);
    }

    void InitRedir(Color color, int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(color, node.getChild(Color.None));
            node.setChild(Color.None, null);
            node.setChild(color.dual(), null);
        }
        minimize();
    }

    // check if by using these the graph is more compact
    private void destRedirNaite(Color color, int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(color));
            node.setChild(color.dual(), node.getChild(color));
            node.setChild(color, null);
        }
        minimize();
    }

    private void destRedirStraite(Color color, int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(color));
            node.setChild(Color.Black, null);
            node.setChild(Color.White, null);
        }
        minimize();
    }

    void destRedirNaiteBlack(int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.Black));
            node.setChild(Color.White, node.getChild(Color.Black));
            node.setChild(Color.Black, null);
        }
        minimize();
    }

    void destRedirNaiteWhite(int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.White));
            node.setChild(Color.Black, node.getChild(Color.White));
        }
        minimize();
    }

    void destRedirStraiteBlack(int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.Black));
            node.setChild(Color.Black, null);
            node.setChild(Color.White, null);
        }
        minimize();
    }

    void destRedirStraiteWhite(int depth) {
        final List<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.White));
            node.setChild(Color.Black, null);
        }
        minimize();
    }

    private Node getLast() {
        Set<Node> current = new HashSet<Node>(1);
        current.add(this);
        while (true) {
            final Set<Node> next =
                    new HashSet<Node>(current.size());
            for (Node node : current) {
                for (Color color : Color.values()) {
                    final Node child = node.getChild(color);
                    if (child != null) {
                        next.add(child);
                    }
                }
            }
            if (next.isEmpty()) {
                return current.toArray(new Node[1])[0];
            }
            current = next;
        }
    }

    private void fuse(Tafa other) {
        Node lastOne = getLast();
        for (Color color : Color.values()) {
            lastOne.setChild(color, other.getChild(color));
        }
        length = length + other.getLength();
    }

    void finalLengthen() {
        fuse(Tafactory.finalGrowth(size));
    }

    private void lengthen(int length) {
        fuse(Tafactory.growth(size, length));
    }

    void lengthen() {
        lengthen(size);
    }

    Tafa copy() {
        Map<Node, Node> layer = new HashMap<Node, Node>();
        final Tafa ans = new Tafa(size, length);
        layer.put(this, ans);
        while (!layer.isEmpty()) {
            final Map<Node, Node> next = new HashMap<Node, Node>(layer.size());
            for (Map.Entry<Node, Node> entry : layer.entrySet()) {
                final Node old = entry.getKey();
                final Node niu = entry.getValue();
                for (Color color : Color.values()) {
                    final Node child = old.getChild(color);
                    if (child != null) {
                        Node niuChild;
                        if (next.containsKey(child)) {
                            niuChild = next.get(child);
                        } else {
                            niuChild = new LittleNode();
                            next.put(child, niuChild);
                        }
                        niu.setChild(color, niuChild);
                    }
                }
            }
            layer = next;
        }
        return ans;
    }

    public Tafa littleCopy() {
        Map<Node, Node> layer = new HashMap<Node, Node>();
        final Tafa ans = new Tafa(size, length);
        layer.put(this, ans);
        while (!layer.isEmpty()) {
            final Map<Node, Node> next = new HashMap<Node, Node>(layer.size());
            for (Map.Entry<Node, Node> entry : layer.entrySet()) {
                final Node old = entry.getKey();
                final Node niu = entry.getValue();
                for (Color color : Color.values()) {
                    final Node child = old.getChild(color);
                    if (child != null) {
                        Node niuChild;
                        if (next.containsKey(child)) {
                            niuChild = next.get(child);
                        } else {
                            niuChild = new LittleNode();
                            next.put(child, niuChild);
                        }
                        niu.setChild(color, niuChild);
                    }
                }
            }
            layer = next;
        }
        return ans;
    }

    private Stack<List<Node>> makeLayerStack() {
        final Stack<List<Node>> layers = new Stack<List<Node>>();
        List<Node> current = new ArrayList<Node>(1);
        current.add(this);
        while (!current.isEmpty()) {
            layers.push(current);
            final Map<Node, Node> next =
                    new IdentityHashMap<Node, Node>(current.size());
            for (Node node : current) {
                for (Color color : Color.values()) {
                    final Node child = node.getChild(color);
                    if (child != null) {
                        next.put(child, child);
                    }
                }
            }
            current = new ArrayList<Node>(next.keySet());
        }
        return layers;
    }

    private static Map<Node, Node> makeMap(List<Node> list) {
        final Map<Node, Node> ans = new HashMap<Node, Node>(list.size());
        for (Node node : list) {
            if (node == null) {
                throw new RuntimeException("eeeeeeeeeeeeeeeeeeeeeeeee");
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

    void minimize() {
        System.out.print("<");
        final Stack<List<Node>> layers = makeLayerStack();
        Map<Node, Node> after = makeMap(layers.pop());
        boolean flag = false;
        while (!layers.isEmpty()) {
            //System.out.println("new layer");
            //print();
            final List<Node> layer = layers.pop();
            for (Node node : layer) {
                for (Color color : Color.values()) {
                    if (node.getChild(color) != null && after.get(node.getChild(color)) == null) {
                        throw new RuntimeException("aaaaaaaa");
                    }
                    node.setChild(color, after.get(node.getChild(color)));
                }
            }
            //System.out.println("after.size:"+after.size());
            //System.out.println("post compactification");
            //print();
            if (flag) {
                for (Node node : layer) {
                    for (Color color : Color.values()) {
                        Node child = node.getChild(color);
                        if (child != null &&
                                child.hashCode() == cdsCode) {


                            node.setChild(color, null);
                            //System.out.println("plop");
                            //print();
                        }
                    }
                }
            } else {
                flag = true;
            }
            after = makeMap(layer);
        }
        System.out.print(">");
    }

    void minimizeOld() {
        final Stack<List<Node>> layers = makeLayerStack();
        int size = 1;
        int flag = 0;
        while (!layers.isEmpty()) {
            final List<Node> layer = layers.pop();
            final Map<Node, Node> map = new HashMap<Node, Node>(size);
            if (flag > 1) {
                for (Node node : layer) {
                    for (Color color : Color.values()) {
                        Node child = node.getChild(color);
                        if (child != null &&
                                child.hashCode() == cdsCode) {
                            node.setChild(color, null);
                        }
                    }
                }
            }
            for (Node node : layer) {
                if (map.containsKey(node)) {
                    node.become(map.get(node));
                } else {
                    map.put(node, node);
                }
            }
            size = map.size();
            flag++;
        }
    }

    private void unvisitRec() {// only need it for printing ...
        // but the visited fields are still needed for merge
        List<Node> current = new ArrayList<Node>(1);
        current.add(this);
        while (!current.isEmpty()) {
            final Map<Node, Node> next =
                    new IdentityHashMap<Node, Node>(current.size());
            for (Node node : current) {
                node.unvisit();
                for (Color color : Color.values()) {
                    final Node child = node.getChild(color);
                    if (child != null) {
                        next.put(child, child);
                    }
                }
            }
            current = new ArrayList<Node>(next.keySet());
        }
    }

	/*public void solidify() {
		unvisitRec();
		super.solidify();
	}*/

    public void printOld() {
        unvisitRec();
        System.out.println("digraph Tafa {");
        super.print();
        System.out.println("}");
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
        final Stack<Color> ans = new Stack<Color>();
        final int size = list.size();
        for (int i = 1; i <= size; i++) {
            ans.push(list.get(size - i));
        }
        return ans;
    }

    static Set<Stack<Color>> transe(Set<List<Color>> set) {
        final Set<Stack<Color>> ans = new HashSet<Stack<Color>>(set.size());
        for (List<Color> list : set) {
            ans.add(trans(list));
        }
        return ans;
    }

    Set<ColorList> getPatterns() {
        return getPatterns(this, new HashSet<Node>(), this.length);
    }

    /**
     * @param current
     * @param elders
     * @param height
     * @return a set of all maximal patterns represented by the dag rooted at current, except those
     * represented by the dags rooted in the elders
     */
    private static Set<ColorList> getPatterns(Node current, Set<Node> elders, int height) {

        // if current is an elder, return an empty set
        if (elders.contains(current)) {
            return new HashSet<>(0);
        }

        // if we are at the end of the dag, return the pattern of length zero
        if (height == 0) {
            HashSet<ColorList> ans = new HashSet<>(); {
                ans.add(new ColorList());
            }
            return ans;
        }

        final Map<Color, Set<ColorList>> childPatterns = new EnumMap<>(Color.class);

        final Node blackChild = current.getChild(Color.Black);
        final Set<ColorList> blackPatterns;
        if (blackChild != null) {
            final Set<Node> blackElders = getChildren(elders, Color.Black);
            blackPatterns = getPatterns(blackChild, blackElders, height - 1);
        } else {
            blackPatterns = new HashSet<>(0);
        }

        final Node noneChild = current.getChild(Color.None);
        final Set<ColorList> nonePatterns;
        if (noneChild != null) {
            final Set<Node> noneElders = getChildren(elders, Color.None);
            if (blackChild != null) {
                noneElders.add(blackChild);
            }
            nonePatterns = getPatterns(noneChild, noneElders, height - 1);
        } else {
            nonePatterns = new HashSet<>(0);
        }

        nonePatterns.removeAll(blackPatterns);

        final Node whiteChild = current.getChild(Color.White);
        final Set<ColorList> whitePatterns;
        if (whiteChild != null) {
            final Set<Node> whiteElders = getChildren(elders, Color.White);
            if (noneChild != null) {
                whiteElders.add(noneChild);
            }
            whitePatterns = getPatterns(whiteChild, whiteElders, height - 1);
        } else {
            whitePatterns = new HashSet<ColorList>(0);
        }

        whitePatterns.removeAll(nonePatterns);
        whitePatterns.removeAll(blackPatterns);

        final Set<ColorList> patterns = new HashSet<ColorList>();

        for (ColorList list : blackPatterns) {
            list.push(Color.Black);
            patterns.add(list);
        }
        for (ColorList list : nonePatterns) {
            list.push(Color.None);
            patterns.add(list);
        }
        for (ColorList list : whitePatterns) {
            list.push(Color.White);
            patterns.add(list);
        }

        return patterns;
    }

    private static Set<Node> getChildren(Collection<Node> nodes, Color color) {
        return nodes.stream()
                .map(node -> node.getChild(color))
                .filter(child -> child != null)
                .collect(Collectors.toSet());
    }

    // memory hungry
    Set<Stack<Color>> getPatternsOld() {
        final Stack<List<Node>> layers = makeLayerStack();
        Map<Node, Set<ColorList>> listMap =
                new HashMap<Node, Set<ColorList>>(1);
        Set<ColorList> niu = new HashSet<ColorList>(1);
        niu.add(new ColorList());
        listMap.put(layers.pop().get(0), niu);
        while (!layers.isEmpty()) {
            final List<Node> layer = layers.pop();
            final Map<Node, Set<ColorList>> next =
                    new HashMap<Node, Set<ColorList>>(layer.size());
            for (Node node : layer) {
                final Set<ColorList> nodeSet = new HashSet<ColorList>();
                Node child = node.getChild(Color.White);
                Set<ColorList> whites;
                if (child != null) {
                    whites = copy(listMap.get(child));
                } else {
                    whites = new HashSet<ColorList>(0);
                }
                child = node.getChild(Color.None);
                Set<ColorList> nones;
                if (child != null) {
                    nones = copy(listMap.get(child));
                } else {
                    nones = new HashSet<ColorList>(0);
                }
                child = node.getChild(Color.Black);
                Set<ColorList> blacks;
                if (child != null) {
                    blacks = copy(listMap.get(child));
                } else {
                    blacks = new HashSet<ColorList>(0);
                }

                whites.removeAll(nones);
                final int before = whites.size();
                whites.removeAll(blacks);
                if (before != whites.size()) {
                    throw new RuntimeException("unexpected shrinking");
                }
                nones.removeAll(blacks);
                for (ColorList stack : whites) {
                    stack.push(Color.White);
                    nodeSet.add(stack);
                }
                for (ColorList stack : nones) {
                    stack.push(Color.None);
                    nodeSet.add(stack);
                }
                for (ColorList stack : blacks) {
                    stack.push(Color.Black);
                    nodeSet.add(stack);
                }
                next.put(node, nodeSet);
            }
            listMap = next;
        }
        final Set<Stack<Color>> ans = transe(trans(listMap.get(this)));
        System.out.println("There are " + ans.size() + " patterns:");
        return ans;
    }

    void printPatterns() {
        final Set<Stack<Color>> patterns = transe(trans(getPatterns()));
        System.out.println("There are " + patterns.size() + " patterns:");
        for (Stack<Color> stack : patterns) {
            System.out.println(ASCIIBoardViewer.viewBoardEnd(size, stack));
        }
    }
}















