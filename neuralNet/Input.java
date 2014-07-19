package neuralNet;


final class Input {

	double rate;
	double weight;
	final Neuron neuron;
	
	Input(double rate, double weight, Neuron neuron) {
		this.rate = rate;
		this.weight = weight;
		this.neuron = neuron;
	}
	
}
