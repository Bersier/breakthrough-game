package patterns.node;

import breakthrough.Color;

public final class WrapperNode implements Node {

	private Node myNode;
	
	WrapperNode(Node node) {
		this.myNode = node;
	}
	public WrapperNode() {
		this(new INode());
	}
	
	@Override
	public boolean equals(Object o) {
		return myNode.equals(((WrapperNode)o).getNode());
	}
	
	@Override
	public boolean isSame(Node node) {
		if(node == null) {
			return false;
		} else if(myNode == node.getNode()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return myNode.hashCode();
	}
	
	@Override
	public int oldHash() {
		return myNode.oldHash();
	}
	
	@Override
	public String toString() {
		return myNode.toString();
	}
	
	@Override
	public Node getNode() {
		return myNode;
	}
	
	@Override
	public void become(Node node) {
		myNode = node.getNode();
	}
	
	@Override
	public Node getChild(Color color) {
		return myNode.getChild(color);
	}
	
	@Override
	public void setChild(Color color, Node child) {
		myNode.setChild(color, child);
	}

	@Override
	public void merge(Node node1, Node node2) {
		myNode.merge(node1, node2);
	}
	
	@Override
	public void unvisit() {
		myNode.unvisit();
	}
	
	public void isLeaf() {
		//return myNode.isLeaf();
	}
	
	@Override
	public void solidify() {
		myNode.solidify();
	}
	
	@Override
	public void print() {
		myNode.print();
	}

}
