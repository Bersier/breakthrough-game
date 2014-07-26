package machineLearning;

public interface Memory<Obs, StateReconstruction, Correction> {

	StateReconstruction generate(Obs o);
	
	void learn(Correction c);
}
