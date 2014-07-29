package neuralNet;

final class InputNeuron implements Neuron {
	
	private double value;
	private double preDelta;
	
	InputNeuron() {
		this.value = 0.0;
	}
	
	void setOutput(double output) {
		value = output;
	}
	
	public double getOutput() {
		return value;
	}
	
	// return and reset preDelta
	public final double preDelta() {
		double temp = preDelta;
		preDelta = 0.0;
		return temp;
	}
	
	public void updatePreDelta(double quantity) {
		preDelta += quantity;
	}
}
