package commons;

/**
 * @param <Obs> the type of the observations to evaluate
 */
@FunctionalInterface
public interface ValueFunction<Obs> {

    /**
     * @return the value of the given observation
     */
	double at(Obs o);

    /**
     * Learn that the given observation has the given value this time.
     * The same observation could be given different values through this method,
     * in which case the learner should try to learn the average value, or expected utility.
     *
     * @param o observation
     * @param value value of the {@link Obs}
     */
    default void learn(Obs o, double value) {
        throw new UnsupportedOperationException("This value function cannot learn!");
    }
}
