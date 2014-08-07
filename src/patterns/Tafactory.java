package patterns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;

import breakthrough.Color;
import commons.Pair;
import patterns.node.LittleNode;
import patterns.node.Node;

import static commons.Utils.Pair;

/*
 * The algorithm:
 *   construct base graph:
 *     
 *   construct the next graph:
 *   if white:
 *     take the union of all the white ...
 * 
 */
/*
 * changed minimization fn and
 * just replaced WrapperNodes by LittleNodes,
 * wasn't the right time to do it.
 * crashes at some point if run
 * use copy 6 to see stuff as it was before the WrapperNode replacement
 * Need to see if stuff is working as it should
 * implement print for LittleNodes
 * implement a node count fn
 * check if graphs make sense
 * implement the extract patterns fn
 * check if patterns make sense
 * refactor
 */

/**
 * Contains functions to compose Tafas.
 */
public final class Tafactory {

    /**
     * @return a new tafa that represents the union of the sets represented by the two given tafas
     */
	public static Tafa union(Tafa node1, Tafa node2) {

        // base cases //todo node that here we don't make a copy
		if(node1 == null) {
			return node2;
		} else if(node2 == null) {
			return node1;
		}

		//System.out.println("node1");
		//node1.print();

		final Tafa ans = new Tafa(node1.getSize(), node1.getLength());

		Map<Pair<Node, Node>, Node> layer = new HashMap<>(1); {
            layer.put(new Pair<>(node1, node2), ans);
        }
		while(!layer.isEmpty()) {//todo looks like merge()!
			final Map<Pair<Node, Node>, Node> next = new HashMap<>(layer.size());

			for(Map.Entry<Pair<Node, Node>, Node> entry : layer.entrySet()) {
				final Node one = entry.getKey().first;
				final Node two = entry.getKey().second;
				final Node niu = entry.getValue();

				for(Color color : Color.values()) {
					final Node childOne = one.getChild(color);
					final Node childTwo = two.getChild(color);

					if(childOne == null) {
						niu.setChild(color, childTwo);

					} else if(childTwo == null) {
						niu.setChild(color, childOne);

					} else {
						final Pair<Node, Node> pair = Pair(childOne, childTwo);
						Node niuChild = next.get(pair);
						if(niuChild == null) {
							niuChild = new LittleNode();
							next.put(pair, niuChild);
						}
						niu.setChild(color, niuChild);
					}
				}
				layer = next;
			}
		}
		ans.minimize();
		return ans;
	}
	
	static Tafa intersection(Tafa node1, Tafa node2) {


		if(node1 == null) {
			return node2;
		} else if(node2 == null) {
			return node1;
		}

		//System.out.println("node1");
		//node1.print();

		final Tafa ans = new Tafa(node1.getSize(), node1.getLength());
		Map<Pair<Node, Node>, Node> layer = new HashMap<>(1);
		layer.put(Pair(node1, node2), ans);
		while(!layer.isEmpty()) {
			final Map<Pair<Node, Node>, Node> next = new HashMap<>(layer.size());
			for(Map.Entry<Pair<Node, Node>, Node> entry : layer.entrySet()) {
				final Node one = entry.getKey().first;
				final Node two = entry.getKey().second;
				final Node niu = entry.getValue();
				for(Color color : Color.values()) {
					final Node childOne = one.getChild(color);
					final Node childTwo = two.getChild(color);
					if(childOne == null || childTwo == null) {
						niu.setChild(color, null);
					} else {
						final Pair<Node, Node> pair = Pair(childOne, childTwo);
						Node niuChild = next.get(pair);
						if(niuChild == null) {
							niuChild = new LittleNode();
							next.put(pair, niuChild);
						}
						niu.setChild(color, niuChild);
					}
				}
				layer = next;
			}
		}
		ans.minimize();
		return ans;
	}
	
	//static int gg = 0;
	static Tafa complement(Tafa theNode) {
		//if(flag&&gg==0) {System.out.println("theNode:");theNode.print();}
		List<Node> current = new ArrayList<Node>(1);
		List<Node> previous = null;
		Node currentDark = null;
		final Tafa ans = theNode;
		current.add(ans);
		while(true) {
			final Set<Node> next =
				new HashSet<Node>(current.size());
			final Node nextDark = new LittleNode();
			for(Node node : current) {
				for(Color color : Color.values()) {
					final Node child = node.getChild(color);
					if(child == null) {
						node.setChild(color, nextDark);
					} else {
						next.add(child);
					}
				}
			}
			if(next.isEmpty()) {
				break;
			} else {
				next.add(nextDark);
				currentDark = nextDark;
				previous = current;
				current = new ArrayList<Node>(next);
			}
		}
		try{
		for(Node node : previous) {
			for(Color color : Color.values()) {
				if(node.getChild(color) != currentDark) {
					node.setChild(color, null);
				}
			}
		}
		} catch(NullPointerException ex) {
			System.out.println("the naughties:");
			for(Node node : current) {
				node.print();
				throw new RuntimeException("caught");
			}
		}
		//ans.print();
		ans.minimize();
		if(ans.graphSize()==1) {
			System.out.println("h");
		}
		return ans;
	}
	
	static Tafa intersectionOld(Tafa node1, Tafa node2) {
		if(node1 == null) {
			return node2;
		} else if(node2 == null) {
			return node1;
		}
		//if(true){union(complement(node1),complement(node2)).print();int e=1/0;}
		//System.out.println("1cu");
		//node1.print();
		//complement(node1).print();
		return complement(
				union(complement(node1),
						complement(node2)));
	}
	
	static Tafa blackElse(int size, int depth, int length) {
		final Tafa ans = growth(size, length);

        // (there is only one node in the layer)
        for (Node node : ans.getLayer(depth)) {
            node.setChild(Color.Black, null);
        }

		return ans;
	}
	
	static Tafa blackElseDestNaite(int size, int depth, int length) {
		final Tafa ans = growth(size, length);

        // (there is only one node in the layer)
        for (Node node : ans.getLayer(depth)) {
            node.setChild(Color.White, null);
            node.setChild(Color.None, null);
        }

		return ans;
	}
	
	static Tafa blackElseDestStraite(int size, int depth, int length) {
		final Tafa ans = growth(size, length);

        // (there is only one node in the layer)
        for (Node node : ans.getLayer(depth)) {
            node.setChild(Color.None, null);
        }

		return ans;
	}
	
	static Tafa growth(int size, int length) {
		final Tafa head = new Tafa(size, length);
		Node current = head;
		for(int i = 0; i < length; i++) {
			final Node next = new LittleNode();
			for(Color color : Color.values()) {
				current.setChild(color, next);
			}
			current = next;
		}
		return head;
	}
	
	static Tafa finalGrowth(int size) {
		final Tafa head = new Tafa(size, size);
		Node current = head;
		for(int i=0; i<size; i++) {
			final Node next = new LittleNode();
			current.setChild(Color.White, next);
			current.setChild(Color.None, next);
			current = next;
		}
		return head;
	}
	
	private static Node aCase(Color color, List<Node> current) {
		final Node next = new LittleNode();
		for(Node node : current) {
			node.setChild(color, next);
		}
		return next;
	}
	
	public static Tafa generateTafa(int size, List<Color> list) {
		final Tafa head = new Tafa(size, list.size());
		List<Node> current = new ArrayList<Node>(3);
		current.add(head);
		for(Color color : list) {
			final List<Node> next = new ArrayList<Node>(3);
			switch(color) {
			case Black:
				next.add(aCase(Color.Black, current));
			case None:
				next.add(aCase(Color.None, current));
			case White:
				next.add(aCase(Color.White, current));
			}
			current = next;
		}
		head.minimize();
		return head;
	}
	
	private static Tafa caseTafa(int size, int depth) {
		final Tafa ans = growth(size, size);

        // (there is only one node in the layer)
        for (Node node : ans.getLayer(depth)) {
            node.setChild(Color.Black, null);
            node.setChild(Color.None, null);
        }

		return ans;
	}
	
	public static <A> List<A> reverse(List<A> list) {
		final int size = list.size();
		final List<A> ans = new ArrayList<A>(size);
		for(int i=1; i<=size; i++) {
			ans.add(list.get(size-i));
		}
		return ans;
	}
	
	static Tafa genTafa(int size, Set<Stack<Color>> set) {
		Tafa ans = null;
		for(List<Color> list : set) {
			list = reverse(list);
			ans = union(ans, generateTafa(size, list));
		}
		return ans;
	}

    /**
     *
     * @param size
     * @return
     */
	static Tafa baseCase(int size) {
		Tafa ans = caseTafa(size, 0);
		for(int i = 1; i < size; i++) {
			ans = union(ans, caseTafa(size, i));
		}
		//ans.print();
		return ans;
	}
	
	private static Tafa forUse(Tafa tafa) {
		if(tafa == null) {
			return null;
		}
		tafa = tafa.copy();
		final int size = tafa.getSize();
		if(tafa.getDepth() < size-1) {
			tafa.lengthen();
		} else if(tafa.getDepth() == size-1) {
			tafa.finalLengthen();
		}
		return tafa;
	}
	
	private static Tafa nextWhite(Tafa tafa, Tafa previous) {
		final int size = tafa.getSize();
		Tafa ans = forUse(previous);

        /* If this is not the first time we recurse, we don't consider the penultimate
         * row as an optimization, as
         */
		final int beginning = (ans == null) ? size : 2*size;

		final int length = (ans == null) ? 2*size : ans.getLength();
		for(int start = beginning; start < length; start++) {
			int dest;
			if(start%size != 0) {
				dest = start - size - 1;
				final Tafa morel = forUse(tafa);
				morel.startRedir(Color.White, start);
				morel.destRedirNaiteWhite(dest);
				ans = union(ans, morel);
				System.out.print(".");
			}
			dest = start - size;
			final Tafa mores = forUse(tafa);
			mores.startRedir(Color.White, start);
			mores.destRedirStraiteWhite(dest);
			ans = union(ans, mores);
			System.out.print(",");
			if(start%size != size-1) {
				dest = start - size + 1;
				final Tafa morer = forUse(tafa);
				morer.startRedir(Color.White, start);
				morer.destRedirNaiteWhite(dest);
				ans = union(ans, morer);
				System.out.print(".");
			}
			System.out.println((start-beginning+1));
		}
		System.out.println("size of the graph: "+ans.graphSize());
		//ans.printPatterns();
		//ans.print();
		return ans;
	}
	
	// This gives the choice to black to not move.
	// If black is forced to move, then this algorithm no longer works,
	// though a similar thing could be done..(?)
	// Using it is correct only if the pattern is not longer than
	// half (plus possibly one) the width of the board
    /*
     * I think this is because of what happens in the following case:
     * Imagine I see through tafa magic that I can win in 9 moves, but that the opponent can win in 10.
     * Then it could be that I can only win in 9 moves if I do a specific move which, at the same time, allows the opponent to win in 8 moves!
     */
	//static int tt = 0;/////////////////////////
	//static boolean flag;///////////////////////
	private static Tafa nextBlack(Tafa tafa) {
		//System.out.println("tafa");
		//tafa.print();
	  //System.gc();
		Tafa ans = tafa.copy();
		final int size = tafa.getSize();
		final int length = tafa.getLength();
		final int end = Math.min(size*(size-2), length-size);
		for(int start=0; start<end; start++) {
			int dest;
			Tafa temp = null;
			Tafa bedn;
			if(start%size != 0) {
				dest = start + size - 1;
				final Tafa morel = tafa.copy();
				morel.startRedir(Color.Black, start);
				morel.destRedirNaiteBlack(dest);
				bedn = blackElseDestNaite(size, dest, length);
				temp = intersection(temp, union(morel, bedn));	
				System.out.print(".");
			}
			dest = start + size;
			//System.gc();
			final Tafa mores = tafa.copy();
			mores.startRedir(Color.Black, start);
			mores.destRedirStraiteBlack(dest);
			bedn = blackElseDestStraite(size, dest, length);
			temp = intersection(temp, union(mores, bedn));
			System.out.print(",");
			if(start%size != size-1) {
				dest = start + size + 1;
				//System.gc();
				final Tafa morer = tafa.copy();
				morer.startRedir(Color.Black, start);
				morer.destRedirNaiteBlack(dest);
				bedn = blackElseDestNaite(size, dest, length);
				temp = intersection(temp, union(morer, bedn));
				System.out.print(".");
			}
			final Tafa blackElse =
				blackElse(size, start, length);
			ans = intersection(ans, union(temp, blackElse));
			//tt++;
			System.out.println((start+1));
		}
		System.out.println("size of the graph: "+ans.graphSize());
		//ans.printPatterns();
		//ans.print();
		return ans;
	}
	
	static Tafa next(Color turn, Tafa tafa, Tafa previous) {
		switch(turn) {
		case White: {
            final Tafa next = nextWhite(tafa, previous);
            //next.printPatterns();
            //next.printPatterns2();
            //next.getPatterns();
            System.out.println("There are " + next.getPatterns().size() + " patterns");
            //next = genTafa(next.getSize(), next.getPatterns());
            //System.out.println("size after compactification: "+next.size());
            return next;
        }
		case Black: {
            final Tafa next = nextBlack(tafa);
            //next.printPatterns();
            //next.printPatterns2();
            //next.getPatterns();
            System.out.println("There are " + next.getPatterns().size() + " patterns");
            //next = genTafa(next.getSize(), next.getPatterns());
            //System.out.println("size after compactification: "+next.size());
            return next;
        }
		default:
			throw new RuntimeException("It's nobody's turn");
		}
	}

    /**
     *
     * @param size
     * @param N
     * @return a list of all tafas for a board of the given size for each number of moves up to N
     */
	static List<Tafa> tafas(int size, int N) {
		final Stack<Tafa> ans = new Stack<>();
		Tafa previous = baseCase(size);
		previous.getPatterns();
		ans.push(previous);
		previous = null;
		Color color = Color.White;
		for(int i = 1; i <= N; i++) {
			final Tafa next = next(color, ans.peek(), previous);
			System.out.println(i+" moves ahead ("+color+").\n");
			previous = ans.peek();
			ans.push(next);
			color = color.dual();
		}
		return ans;
	}
	
	public static void main(String[] args) {
	  System.out.println(java.lang.Runtime.getRuntime().maxMemory());
		System.out.println("start");
		final List<Tafa> list = tafas(8, 40);
		//list.get(3).print();
		System.out.println("done");
	}
}














