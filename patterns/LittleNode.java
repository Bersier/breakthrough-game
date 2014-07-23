package patterns;

import breakthrough.Color;

final class LittleNode implements Node {

	private Node white;
	private Node black;
	private Node none;
	
	@Override
	public void become(Node node) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Node getChild(Color color) {
		switch(color) {
		case White:
			return white;
		case Black:
			return black;
		default:
			return none;
		}
	}

	@Override
	public Node getNode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void merge(Node node1, Node node2) {
		throw new UnsupportedOperationException();

	}
	
	
	public final int hashCodeOld() {
		int ans = 0;
		int i = 0;
		for(Color color : Color.values()) {
			final Node child = getChild(color); 
			if(child == null) {
				ans ^= Hash.rotl(INode.code, i);
			} else {
				ans ^= Hash.rotl(child.oldHash(), i);
			}
			i++;
		}
		return ans;
	}
	
	@Override
	public final int hashCode() {
		Node child = getChild(Color.White);
		int ans = (child==null) ? INode.code : child.oldHash();
		child = getChild(Color.Black);
		ans ^= Hash.rotl((child==null) ? INode.code : child.oldHash(), 1);
		child = getChild(Color.None);
		ans ^= Hash.rotl((child==null) ? INode.code : child.oldHash(), 2);
		return ans;
	}
	
	@Override
	public int oldHash() {
		return super.hashCode();
	}
	
	@Override
	public boolean isSame(Node node) {
		if(node == null) {
			return false;
		} else {
			return this == node;
		}
	}
	
	public boolean equalsOld(Object o) {
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
	
	@Override
	public boolean equals(Object o) {
		final Node node = (Node)o;
		Node child = getChild(Color.White);
		Node other = node.getChild(Color.White);
		if(child == null) {
			if(other != null) {
				return false;
			}
		} else if(!child.isSame(other)) {
			return false;
		}
		child = getChild(Color.Black);
		other = node.getChild(Color.Black);
		if(child == null) {
			if(other != null) {
				return false;
			}
		} else if(!child.isSame(other)) {
			return false;
		}
		child = getChild(Color.None);
		other = node.getChild(Color.None);
		if(child == null) {
			if(other != null) {
				return false;
			}
		} else if(!child.isSame(other)) {
			return false;
		}
		return true;
	}

	@Override
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

	@Override
	public void setChild(Color color, Node node) {
		switch(color) {
		case White:
			white = node;
		break;
		case Black:
			black = node;
		break;
		default:
			none = node;
		break;
		}
	}

	@Override
	public void solidify() {
		throw new UnsupportedOperationException();

	}

	@Override
	public void unvisit() {
		throw new UnsupportedOperationException();

	}

}
