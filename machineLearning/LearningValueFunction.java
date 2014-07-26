package machineLearning;

import breakthrough.ValueFunction;

public interface LearningValueFunction<Obs> extends ValueFunction<Obs> {

    @Override
	double at(Obs o);

    /**
     * Give feedback to the learner.
     * Tell it that the last observation it evaluated should have been given the given value.
     *
     * @param value the value of the last {@link Obs}
     */
	void learn(double value);

    /**
     * Learn that a given observation has a given value.
     *
     * @param o observation
     * @param value value of the {@link Obs}
     */
	void learn(Obs o, double value);
}
