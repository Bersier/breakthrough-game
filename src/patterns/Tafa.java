package patterns;

import breakthrough.Color;
import breakthrough.game.ASCIIBoardViewer;
import patterns.node.INode;
import patterns.node.LittleNode;
import patterns.node.Node;

import java.util.*;
import java.util.Set;
import java.util.Stack;

import static commons.Utils.List;
import static commons.Utils.Set;

/**
 * Represents a set of winning boards.
 * To test whether a board belongs to the set, the colors of the last 'length' squares of the board
 * on the right (ie the last length / size (= depth) columns) are put into a list.
 * This list is seen as a word, and this Node is seen as the initial state of a finite-state
 * automaton (which also happens to be a dag).
 * If the automaton doesn't get stuck before the end of the word (because there is no edge of the
 * needed color), then the board belongs to the set.
 *
 * The dag has additional properties, if constructed properly (and it always should be):
 * The depth of a node is well-defined. Ie, all paths to a given node have the same length.
 * From any node, if a word is accepted, a weaker word is accepted too. In particular,
 * replacing the first color of the word by a lighter one.
 *
 * The ordering on words is given by the product of the ordering: Black > None > White
 */
public final class Tafa extends INode implements commons.Set<Tafa> {// replace ArrayLists by Stacks?

    @Override
    public Tafa union(commons.Set<Tafa> set) {
        return null;
    }//todo does this actually buy us any abstraction?

    @Override
    public Tafa intersection(commons.Set<Tafa> set) {
        return null;
    }

    @Override
    public Tafa complement() {
        return Tafactory.complement(this);
    }


    /**
     * The length of the dag (number of edges to follow to reach end)
     */
    private int length;

    /**
     * The size of boards this tafa was made for
     */
    private final int size;

    public Tafa(int size, int length) {
        this.length = length;
        this.size = size;
    }

    /**
     * @return The number of columns to be fed (as a list) to this tafa
     */
    int getDepth() {
        return length / size;
    }

    /**
     * @return the size of boards this tafa was made for
     */
    int getSize() {
        return size;
    }

    /**
     * @return the length of the dag (number of edges to follow to reach end)
     */
    int getLength() {
        return length;
    }

    /**
     * @return the number of nodes reachable from this one
     */
    int graphSize() {//todo move to Node
        int count = 0;

        // keeps the nodes reachable from this node in 0, 1, 2, ... steps
        Set<Node> current = new HashSet<>(1); {
            current.add(this);
        }

        // keep going as long as possible
        while (!current.isEmpty()) {
            count += current.size();

            // update current to contain the next layer
            current = Utils.getChildren(current);
        }
        return count;
    }

    /**
     * Prints out DOT notation code to represent this graph.
     */
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

    /**
     * @return all nodes at the given depth
     */
    Collection<Node> getLayer(int depth) {

        // set currentLayer to the zeroth layer
        Set<Node> currentLayer = Set(this);

        // update currentLayer to next layer until depth is reached
        for (int i = 0; i < depth; i++) {
            currentLayer = Utils.getChildren(currentLayer);
        }

        return currentLayer;
    }

    /**
     * We suppose the current tafa represents a set of winning boards.
     * Now we are only interested in winning boards that are the result of a move
     * of the given color that started at depth.
     * Therefore, at that depth, the boards must now have an empty square (Color.None).
     * This corresponds to cutting the white and black edges at that depth.
     * Now we want to recognize those boards BEFORE the move.
     * How can we get these from the boards after the move?
     * Instead of an empty square, there must be a pawn of the given color.
     * This corresponds to changing the color of the None-edge to the given color.
     *
     * @param color
     * @param depth
     */
    void startRedir(Color color, int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(color, node.getChild(Color.None));
            node.setChild(Color.None, null);
            node.setChild(color.dual(), null);
        }
        minimize();
    }

    // check if by using these the graph is more compact
    private void destRedirNaite(Color color, int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(color));
            node.setChild(color.dual(), node.getChild(color));
            node.setChild(color, null);
        }
        minimize();
    }

    private void destRedirStraite(Color color, int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(color));
            node.setChild(Color.Black, null);
            node.setChild(Color.White, null);
        }
        minimize();
    }

    void destRedirNaiteBlack(int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.Black));
            node.setChild(Color.White, node.getChild(Color.Black));
            node.setChild(Color.Black, null);
        }
        minimize();
    }

    /**
     * Here, the white edge isn't deleted, because it's a weakening of the other boards.
     * If the stronger boards are winning boards, the weaker ones are too (no Zwangzug).
     *
     * @param depth
     */
    void destRedirNaiteWhite(int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.White));
            node.setChild(Color.Black, node.getChild(Color.White));
        }
        minimize();
    }

    void destRedirStraiteBlack(int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.Black));
            node.setChild(Color.Black, null);
            node.setChild(Color.White, null);
        }
        minimize();
    }

    void destRedirStraiteWhite(int depth) {
        final Collection<Node> layer = getLayer(depth);
        for (Node node : layer) {
            node.setChild(Color.None, node.getChild(Color.White));
            node.setChild(Color.Black, null);
        }
        minimize();
    }

    private Node getLast() {//todo if tafa is well-formed, should be able to take just one node at each layer
        Set<Node> current = Set(this);
        while (true) {
            final Set<Node> next = Utils.getChildren(current);
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
        final Stack<List<Node>> layers = new Stack<>();
        List<Node> current = List(this);
        while (!current.isEmpty()) {
            layers.push(current);
            final Map<Node, Node> next = new IdentityHashMap<>(current.size());
            Utils.getChildrenStream(current).forEach(node -> next.put(node, node));
            current = List(next.keySet());
        }
        return layers;
    }

    /**
     * Keep ALL nodes in a weak hashmap! This way, minimization happens automatically.
     */
    void minimize() {
        System.out.print("<");
        final Stack<List<Node>> layers = makeLayerStack();
        Map<Node, Node> after = Utils.makeMap(layers.pop());
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
            after = Utils.makeMap(layer);
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

    Set<ColorList> getPatterns() {
        return getPatterns(this, new HashSet<Node>(), this.length);
    }

    /**
     * @param current
     * @param elders
     * @param height
     * @return a set of all maximal patterns represented by the dag rooted at current, except those
     * represented by the dags rooted in the elders
     *///todo refactor
    private static Set<ColorList> getPatterns(Node current, Set<Node> elders, int height) {

        // if current is an elder, return an empty set
        if (elders.contains(current)) {
            return Set();
        }

        // if we are at the end of the dag, return the pattern of length zero
        if (height == 0) {
            return Set(new ColorList());
        }

        final Set<ColorList> patterns = new HashSet<>();
        Node elder = null;

        for (Color color : Color.values()) {// note: this is color-order-sensitive
            final Node child = current.getChild(color);
            addPatterns(color, getPatterns(elders, height, elder, color, child), patterns);
            elder = child;
        }

        return patterns;
    }

    private static Set<ColorList> getPatterns(Set<Node> elders, int height, Node elder, Color color, Node child) {
        if (child == null) {
            return Set();
        }

        final Set<Node> newElders = Utils.getChildren(elders, color);
        if (elder != null) {
            newElders.add(elder);
        }
        return getPatterns(child, newElders, height - 1);
    }

    private static void addPatterns(Color color, Set<ColorList> toAdd, Set<ColorList> patterns) {
        for (ColorList list : toAdd) {
            list.push(color);
            patterns.add(list);
        }
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
                    whites = Utils.copy(listMap.get(child));
                } else {
                    whites = new HashSet<ColorList>(0);
                }
                child = node.getChild(Color.None);
                Set<ColorList> nones;
                if (child != null) {
                    nones = Utils.copy(listMap.get(child));
                } else {
                    nones = new HashSet<ColorList>(0);
                }
                child = node.getChild(Color.Black);
                Set<ColorList> blacks;
                if (child != null) {
                    blacks = Utils.copy(listMap.get(child));
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
                addPatterns(Color.White, whites, nodeSet);
                addPatterns(Color.None, nones, nodeSet);
                addPatterns(Color.Black, blacks, nodeSet);
                next.put(node, nodeSet);
            }
            listMap = next;
        }
        final Set<Stack<Color>> ans = Utils.transe(Utils.trans(listMap.get(this)));
        System.out.println("There are " + ans.size() + " patterns:");
        return ans;
    }

    void printPatterns() {
        final Set<Stack<Color>> patterns = Utils.transe(Utils.trans(getPatterns()));
        System.out.println("There are " + patterns.size() + " patterns:");
        for (Stack<Color> stack : patterns) {
            System.out.println(ASCIIBoardViewer.viewBoardEnd(size, stack));
        }
    }
}
