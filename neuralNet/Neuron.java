package neuralNet;

interface Neuron {

	double getOutput();
	
	// used by children to tell parents about delta
	void updatePreDelta(double quantity);
}
