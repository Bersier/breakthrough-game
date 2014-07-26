package neuralNet;

import java.util.List;
import java.text.DecimalFormat;

final class NormalNeuron implements DynamicNeuron {

	private final List<Input> inputs;
	private double output;
	private double preDelta;
	private double delta;
	
	NormalNeuron(List<Input> inputs) {
		this.inputs   = inputs;
		this.output   = 0.0;
		this.preDelta = 0.0;
		this.delta    = 0.0;
	}
	
	public String toString() {
		DecimalFormat f = new DecimalFormat(" #0.00;-#0.00");
		String str ="(";
		int ii = 0;
		for(Input input : inputs) {
			str += f.format(input.weight) + " ";
			if(ii%4==0){
				str += "\n";
			}
			ii++;
		}
		str += ")\n";
		return str;
	}
	
	public double getOutput() {
		return output;
	}
	
	// sigmoid
	public static double squashingFn(double x) {
		return 1.0/(1 + Math.exp(-x));
	}
	
	// update output
	public void fire() {
		double sum = 0.0;
		for(Input input : inputs) {
			sum += input.weight * input.neuron.getOutput();
		}
		output = squashingFn(sum);
	}
	
	// don't use this yet
	void linearFire() {
		double sum = 0.0;
		for(Input input : inputs) {
			sum += input.weight * input.neuron.getOutput();
		}
		output = sum;
	}
	
	// update the weights
	public void computeDeltaAndWeights() {
		delta = /* output * (1-output) **/ preDelta;
		preDelta = 0.0;
		for(Input input : inputs) {
			input.neuron.updatePreDelta(input.weight * delta);
			input.weight += delta * input.rate * input.neuron.getOutput();
		}
	}
	
	// tell parents about delta
	public void updatePreDelta(double quantity) {
		preDelta += quantity;
	}
}
