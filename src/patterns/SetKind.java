package patterns;

public interface SetKind<Kind> {
	
	Kind union(Kind set);
	
	Kind intersection(Kind set);
	
	Kind complement();
	
	Kind copy();
	
}
