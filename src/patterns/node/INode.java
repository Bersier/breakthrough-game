package patterns.node;

import breakthrough.Color;
import commons.Utils;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class INode implements Node {

    final static        int code    = (new Random()).nextInt();
    public final static int cdsCode = (new INode()).hashCode();

    private       boolean          visited  = false;
    private final Map<Color, Node> children = new EnumMap<>(Color.class);


    public final int hashCode() {
        int ans = 0;
        int i = 0;
        for (Color color : Color.values()) {
            final Node child = getChild(color);
            if (child == null) {
                ans ^= Utils.rotl(code, i);
			} else {
				ans ^= Utils.rotl(child.oldHash(), i);
			}
			i++;
		}
		return ans;
	}
	
	public int oldHash() {
		return super.hashCode();
	}

    /**
     * According to this equality definition, and to the hashCode, two nodes are equal if their
     * children are identical.
     * This is the used definition because the current algorithms work layer-wise.
     */
	public boolean equals(Object o) {
		final Node node = (Node)o;
		for(Color color : Color.values()) {
			final Node child = getChild(color); 
			if(child == null) {
				if(node.getChild(color) != null) {
					return false;
				}
			} else if(!child.isSame(node.getChild(color))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isSame(Node node) {
		throw new UnsupportedOperationException();
	}
	
	public final void unvisit() {
		visited = false;
	}
	
	public final Node getChild(Color color) {
		return children.get(color);
	}
	
	public final void setChild(Color color, Node node) {
		children.put(color, node);
	}
	
	public final boolean isLeaf() {
		for(Color color : Color.values()) {
			if(getChild(color) != null) {
				return false;
			}
		}
		return true;
	}
	
	public void merge(Node node1, Node node2) {
		if(!visited) {
			if(node1 == this || node2 == this || node1 == node2) {
				throw new RuntimeException("merging undistinguished nodes");
			}
			for(Color color : Color.values()) {
				final Node child1 = node1.getChild(color);
				final Node child2 = node2.getChild(color);
				if(child1 == null && child2 == null) {
					continue;
				} else if(child1 == null) {
					setChild(color, child2);
				} else if(child2 == null) {
					setChild(color, child1);
				} else {
					getChild(color).merge(child1, child2);
				}
			}
			visited = true;
		}
	}
	
	public Node getNode() {
		throw new UnsupportedOperationException();
	}
	
	public void become(Node node) {
		throw new UnsupportedOperationException();
	}
	
	public void solidify() {
		if(!visited) {
			for(Color color : Color.values()) {
				Node child = getChild(color);
				if(child != null) {
					setChild(color, child.getNode());
					child.solidify();
				}
			}
			visited = true;
		}
	}

	public void print() {
		System.out.println("\""+this.oldHash()+"\" [label=\"\"];");
		for(Color color : Color.values()) {
			Node child = getChild(color);
			if(child != null) {
				switch(color) {
				case White:
					System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=yellow] ;");
					break;
				case None:
					System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=grey] ;");
					break;
				case Black:
					System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=black] ;");
					break;
				}
			}
		}
	}
	
	public void printOld() {
		if(!visited) {
			System.out.println("\""+this.oldHash()+"\" [label=\"\"];");
			for(Color color : Color.values()) {
				Node child = getChild(color);
				if(child != null) {
					switch(color) {
					case White:
						System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=yellow] ;");
					break;
					case None:
						System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=grey] ;");
					break;
					case Black:
						System.out.println("\""+this.oldHash()+"\" -> \""+child.oldHash()+"\" [color=black] ;");
					break;
					}
					child.print();
				}
			}
			visited = true;
		}
	}
	
}
