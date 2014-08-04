package commons;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    /**
     * Convenient for hashing.
     *
     * @param value the int whose bits shall be rotated
     * @param shift the amount by which the bits shall be rotated
     * @return a left-rotation of the bits in value//todo not sure about direction
     */
	public static int rotl(int value, int shift) {
	    return (value << shift) | (value >>> -shift);
	}

    /**
     * @return converts the given expected utility into a winning probability
     */
    public static double vToP(double v) {
        return (v + 1)/2;
    }

    /**
     * @return converts the given winning probability to a utility
     */
    public static double ptoV(double p) {
        return 2*p - 1;
    }

    public static <T> Set<T> Set(T... elements) {
        final Set<T> set = new HashSet<>(elements.length);
        set.addAll(Arrays.asList(elements));
        return set;
    }
}
