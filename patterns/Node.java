package patterns;

import breakthrough.Color;

// add defs to make it WrapperNode-equiv?

interface Node {
	
	Node getChild(Color color);
	
	void setChild(Color color, Node node); 
	
	void unvisit();
	
	void merge(Node node1, Node node2);
	
	int oldHash();
	
	//boolean isLeaf();
	
	Node getNode();
	
	boolean isSame(Node node);
	
	void solidify();
	
	void become(Node node);
	
	void print();
	
}
