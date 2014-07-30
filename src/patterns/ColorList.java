package patterns;

import breakthrough.Color;
import commons.Hash;

import java.util.List;
import java.util.ArrayList;

final class ColorList implements StackFace<Color> {

	private static final int sizeLimit = 64;
	
	private int white1;
	private int white2;
	private int black1;
	private int black2;
	private byte next;
	
	ColorList() {}
	
	ColorList(ColorList list) {
		this.white1 = list.white1;
		this.white2 = list.white2;
		this.black1 = list.black1;
		this.black2 = list.black2;
		this.next = list.next;
	}
	
	public int hashCode() {
		int ans = next;
		ans ^= Hash.rotl(white1, 1);
		ans ^= Hash.rotl(white2, 2);
		ans ^= Hash.rotl(black1, 3);
		ans ^= Hash.rotl(black2, 4);
		return ans;
	}
	
	public boolean equals(Object o) {
		final ColorList other = (ColorList)o;
		return next == other.next &&
			   white1 == other.white1 &&
			   white2 == other.white2 &&
			   black1 == other.black1 &&
			   black2 == other.black2;
	}
	
	List<Color> toList() {
		ColorList list = new ColorList(this);
		final List<Color> ans = new ArrayList<Color>();
		while(!list.isEmpty()) {
			ans.add(list.pop());
		}
		return ans;
	}
	
	private boolean isFull() {
		return next >= sizeLimit;
	}
	
	private boolean isEmpty() {
		return next == 0;
	}

	@Override
	public Color peek() {
		if(isEmpty()) {
			throw new RuntimeException("ColorStack is empty");
		}
		if(next<=sizeLimit/2) {
			boolean isWhite = peek(white1);
			boolean isBlack = peek(black1);
			if(isWhite) {
				return Color.White;
			} else if(isBlack) {
				return Color.Black;
			} else {
				return Color.None;
			}
		} else {
			boolean isWhite = peek(white2);
			boolean isBlack = peek(black2);
			if(isWhite) {
				return Color.White;
			} else if(isBlack) {
				return Color.Black;
			} else {
				return Color.None;
			}
		}
	}

	@Override
	public Color pop() {
		final Color ans = peek();
		if(next<=sizeLimit/2) {
			white1 = pop(white1);
			black1 = pop(black1);
		} else {
			white2 = pop(white2);
			black2 = pop(black2);
		}
		next--;
		return ans;
	}

	private static int put(boolean value, int n, byte position) {
		if(value) {
			n = (1 << position) | n;
		} else {
			n = (~(1 << position)) & n;
		}
		return n;
	}
	
	private static int push(boolean value, int n) {
		if(value) {
			n = (n << 1) | 1;
		} else {
			n = (n << 1);
		}
		return n;
	}
	
	private static boolean peek(int n) {
		final boolean ans = (n & 1) == 1;
		return ans;
	}
	
	private static int pop(int n) {
		return n >>> 1;
	}
	
	@Override
	public void push(Color color) {
		if(isFull()) {
			throw new RuntimeException("ColorStack is full");
		}
		if(next<sizeLimit/2) {
			switch(color) {
			case White:
				white1 = push(true, white1);
				black1 = push(false, black1);
				break;
			case Black:
				white1 = push(false, white1);
				black1 = push(true, black1);
				break;
			default:
				white1 = push(false, white1);
				black1 = push(false, black1);
			}
		} else {
			switch(color) {
			case White:
				white2 = push(true, white2);
				black2 = push(false, black2);
				break;
			case Black:
				white2 = push(false, white2);
				black2 = push(true, black2);
				break;
			default:
				white2 = push(false, white2);
				black2 = push(false, black2);
			}
		}
		next++;
	}
	
	@Override
	public String toString() {
	  String ans = "[ ";
	  ColorList copy = new ColorList(this);
	  while(!copy.isEmpty()) {
	    ans += copy.pop() + " ";
	  }
	  return ans + "]" +" ("+white1+"."+white2+","+black1+"."+black2+";"+next+")";
	}
	
}
