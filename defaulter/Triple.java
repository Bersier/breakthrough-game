package defaulter;


public final class Triple<A, B, C> {
	
	public final A first;
	public final B second;
	public final C third;

	public Triple(A a, B b, C c) {
		this.first = a;
		this.second = b;
		this.third = c;
	}
	
	public static int rotl(int value, int shift) {
	    return (value << shift) | (value >>> -shift);
	}

	public int hashCode() {
		return first.hashCode() ^
		rotl(second.hashCode(), 1) ^
		rotl(third.hashCode(), 2);
	}

	public boolean equals(Object o) {
		final Triple<A, B, C> other = (Triple<A, B, C>)o;
		return first.equals(other.first) &&
			second.equals(other.second) &&
			third.equals(other.third);
	}

	public String toString() {
		return "("+first.toString()+","+second.toString()+third.toString()+")";
	}

}
