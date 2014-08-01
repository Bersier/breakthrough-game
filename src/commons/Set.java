package commons;

public interface Set<A> {
	
	A union(A set);
	
	A intersection(A set);
	
	A complement();
	
	A copy();
}
