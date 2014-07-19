package neuralNet;


interface DynamicNeuron extends Neuron {
	
	// update output
	void fire();
	
	// update the weights
	void computeDeltaAndWeights();
	
}
