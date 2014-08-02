package commons;

public interface Utils {

    /**
     * Convenient for hashing.
     *
     * @param value the int whose bits shall be rotated
     * @param shift the amount by which the bits shall be rotated
     * @return a left-rotation of the bits in value//todo not sure about direction
     */
	static int rotl(int value, int shift) {
	    return (value << shift) | (value >>> -shift);
	}

    /**
     * @return converts the given expected utility into a winning probability
     */
    static double vToP(double v) {
        return (v + 1)/2;
    }

    /**
     * @return converts the given winning probability to a utility
     */
    static double ptoV(double p) {
        return 2*p - 1;
    }
}
