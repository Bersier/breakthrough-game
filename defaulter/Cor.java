package defaulter;
import machineLearning.Evaluator;


final class Cor<Action, Obs> {

	private final double decayRate = 1.0;
	private final State<Action, Obs> state;
	private final Evaluator<Obs> evaluator;
	private final Actor<Action, Obs> actor;
	
	// don't use
	private Cor() {
		state = null;
		evaluator = null;
		actor = null;
	}
	
	void step() {
		double oldValue = evaluator.evaluate(state.observe());
		double reward = state.act(actor.act(state.observe()));
		double value = evaluator.evaluate(state.observe());
		double trendValue = decayRate*value + reward;
		actor.learn(trendValue - oldValue);
		evaluator.delayedLearn(trendValue);
	}
	
}
