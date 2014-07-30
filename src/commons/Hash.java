package commons;

public final class Hash {//todo rename

	public static int rotl(int value, int shift) {
	    return (value << shift) | (value >>> -shift);
	}
	
}
