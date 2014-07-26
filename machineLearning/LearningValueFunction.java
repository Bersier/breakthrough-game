package machineLearning;

import breakthrough.ValueFunction;

@Deprecated
public interface LearningValueFunction<Obs> extends ValueFunction<Obs> {

    @Override
	double at(Obs o);

    /**
     * Learn that the given observation has the given value this time.
     * The same observation could be given different values through this method,
     * in which case the learner should try to learn the average value, or expected utility.
     *
     * @param o observation
     * @param value value of the {@link Obs}
     */
	void learn(Obs o, double value);
}
