package patterns;

public interface StackFace<A> {

	void push(A a);
	
	A pop();
	
	A peek();
	
}
