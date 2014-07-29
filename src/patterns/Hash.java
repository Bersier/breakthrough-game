package patterns;

public final class Hash {

	public static int rotl(int value, int shift) {
	    return (value << shift) | (value >>> -shift);
	}
	
}
