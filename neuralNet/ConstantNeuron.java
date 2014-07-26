package neuralNet;


final class ConstantNeuron implements Neuron {

	private ConstantNeuron() {}
	
	public static ConstantNeuron singleton = new ConstantNeuron();
	
	public double getOutput() {
		return 1.0;
	}
	
	public void updatePreDelta(double quantity) {}
}
