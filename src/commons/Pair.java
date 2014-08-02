package commons;

public final class Pair<A, B> {

	public final A first;
	public final B second;
	
	public Pair(A a, B b) {
		this.first = a;
		this.second = b;
	}
	
	public int hashCode() {
		return first.hashCode() ^ Utils.rotl(second.hashCode(), 1);
	}
	
	public boolean equals(Object o) {
		final Pair<A, B> other = (Pair<A, B>)o;
		return first.equals(other.first) && second.equals(other.second);
	}
	
	public String toString() {
		return "("+first.toString()+","+second.toString()+")";
	}
	
}
