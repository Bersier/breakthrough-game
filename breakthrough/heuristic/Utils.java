package breakthrough.heuristic;

/**
 * <p>
 * Created on 7/27/2014.
 */
public class Utils {

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
}
