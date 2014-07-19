package machineLearning;

public interface Evaluator<Obs> {
	
	double evaluate(Obs o);
	
	void learn(double value);
	
	void learn(Obs o, double value);
	
}
