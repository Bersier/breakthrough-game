package patterns.old;

import breakthrough.Color;
import patterns.Node;
import patterns.Tafa;
import patterns.Tafactory;
import patterns.WrapperNode;

import java.util.List;

final class OldTafa {// implements Set!?

	private Tafa head;
	private final int length;
	
	// size > 1
	// N > 0, or not?
	// linked list has length length+1
	OldTafa(int size, int N) {
		this.length = size * Math.min(size,((N+3)/2));
		this.head = new Tafa(size, length);
		Node current = head;
		for(int i=0; i<length; i++) {
			final WrapperNode next = new WrapperNode();
			current.setChild(Color.White, next);
			current = next;
		}
	}
	OldTafa(Tafa head, int length) {
		this.head = head;
		this.length = length;
	}
	
	boolean accepts(Pattern p) {
		return accepts(p.getList());
	}
	// list has length length
	boolean accepts(List<Color> list) {
		Node current = head;
		int i = 1;
		for(Color color : list) {
			if(i > length) {
				break;
			}
			final Node child = current.getChild(color);
			if(child == null) {
				return false;
			}
			current = child;
			i++;
		}
		return true;
	}
	
	void grow(Pattern p) {
		grow(p.getSize(), p.getList());
	}
	private void grow(int size, List<Color> list) {
		final Tafa other = Tafactory.generateTafa(size, list);
		head = Tafactory.union(head, other);
	}
	
	void solidify() {
		print();
		head = head.littleCopy();
	}
	
	void print() {
		head.print();
	}
	
}
