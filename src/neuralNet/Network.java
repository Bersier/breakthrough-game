package neuralNet;

import breakthrough.ValueFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class Network implements ValueFunction<double[]> {

	final static Random random = new Random();
	private final static double rate = 0.001;//0.05;
	private final List<List<DynamicNeuron>> hiddenLayers;
	private final InputNeuron[] inputNeurons;
	private final DynamicNeuron[] outputNeurons;
	
	public Network(int noOfInputs, int noOfOutputs) {
		this.hiddenLayers = new ArrayList<List<DynamicNeuron>>();
		this.inputNeurons = new InputNeuron[noOfInputs];
		for(int i=0; i<inputNeurons.length; i++) {
			inputNeurons[i] = new InputNeuron();
		}
		this.outputNeurons = new NormalNeuron[noOfOutputs];
		for(int i=0; i<outputNeurons.length; i++) {
			final ArrayList<Input> inputs = new ArrayList<Input>();
			inputs.add(new Input(rate, 0.0, ConstantNeuron.singleton));
			//inputs.add(new Input(rate, random.nextDouble()*2-1, ConstantNeuron.singleton));
			int ii = 0;
			for(Neuron neuron : inputNeurons) {
				//if(ii<16)inputs.add(new Input(rate, 1.0, neuron));
				//else inputs.add(new Input(rate, -1.0, neuron));
				inputs.add(new Input(rate, 0.0, neuron));
				//inputs.add(new Input(rate, random.nextDouble()*2-1, neuron));
				ii++;
			}
			outputNeurons[i] = new NormalNeuron(inputs);
		}
	}
	
	final static double cauchy() {
		return Math.tan(Math.PI*(random.nextDouble()-0.5));
	}
	
	public Network(int noOfInputs, int noOfOutputs, int noOfHiddenLayers) {
		final int hdnLrSize = noOfInputs;
		this.hiddenLayers = new ArrayList<List<DynamicNeuron>>();
		this.inputNeurons = new InputNeuron[noOfInputs];
		for(int i=0; i<inputNeurons.length; i++) {
			inputNeurons[i] = new InputNeuron();
		}
		List<Neuron> previous = new ArrayList<Neuron>();
		previous.addAll(Arrays.asList((Neuron[])inputNeurons));
		for(int k=0; k<noOfHiddenLayers; k++) {
			final List<DynamicNeuron> hiddenLayer =
				new ArrayList<DynamicNeuron>(hdnLrSize);
			double lrRate = rate/Math.pow(1, noOfHiddenLayers-k);
			for(int i=0; i<hdnLrSize; i++) {
				final ArrayList<Input> inputs = new ArrayList<Input>();
				inputs.add(new Input(rate, 0.0, ConstantNeuron.singleton));
				//inputs.add(new Input(lrRate,
				//		(random.nextDouble()*2-1)/hdnLrSize*lrRate*100, ConstantNeuron.singleton));
				int ii = 0;
				for(Neuron neuron : previous) {
					//inputs.add(new Input(rate, 0.0, neuron));
					if(ii<noOfInputs) {
						inputs.add(new Input(lrRate,
								((ii==i)?1:0), neuron));
					} else {
						inputs.add(new Input(lrRate,
								cauchy()/hdnLrSize, neuron));
					}
					ii++;
				}
				hiddenLayer.add(new NormalNeuron(inputs));
			}
			hiddenLayers.add(hiddenLayer);
			previous.addAll(new ArrayList<Neuron>(hiddenLayer));
			//previous = new ArrayList<Neuron>(hiddenLayer);
		}
		this.outputNeurons = new NormalNeuron[noOfOutputs];
		for(int i=0; i<outputNeurons.length; i++) {
			final ArrayList<Input> inputs = new ArrayList<Input>();
			inputs.add(new Input(rate, 0.0, ConstantNeuron.singleton));
			//inputs.add(new Input(rate, random.nextDouble()*2-1, ConstantNeuron.singleton));
			int ii = 0;
			for(Neuron neuron : previous) {
				//if(ii<16)inputs.add(new Input(rate, 1.0, neuron));
				//else inputs.add(new Input(rate, -1.0, neuron));
				inputs.add(new Input(rate, 0.0, neuron));
				//inputs.add(new Input(rate, random.nextDouble()*2-1, neuron));
				ii++;
			}
			outputNeurons[i] = new NormalNeuron(inputs);
		}
	}
	
	public void learn(double desired) {
		final double[] desiredOutput = new double[1];
		desiredOutput[0] = desired;
		learn(desiredOutput);
	}
	
	// unfinished
	public String toString() {
		String str = "";
		for(DynamicNeuron neuron : outputNeurons) {
			str += neuron.toString();
		}
		return str;
	}
	
	public void learn(double[] inputs, double desired) {
		final double[] desiredOutput = new double[1];
		desiredOutput[0] = desired;
		delayedLearn(inputs, desiredOutput);
	}
	
	public double at(double[] inputs) {
		return evaluateGen(inputs)[0];
	}
	
	// evaluate network for inputValues
	public double[] evaluateGen(double[] inputValues) {
		if(inputValues.length != inputNeurons.length) {
			throw new RuntimeException("input length doesn't mach network");
		}
		// forward pass
		for(int i=inputNeurons.length-1; i>=0; i--) {
			inputNeurons[i].setOutput(inputValues[i]);
		}
		for(List<DynamicNeuron> layer : hiddenLayers) {
			for(DynamicNeuron neuron : layer) {
				neuron.fire();
			}
		}
		double[] output = new double[outputNeurons.length];
		int i = 0;
		for(DynamicNeuron neuron : outputNeurons) {
			neuron.fire();
			output[i] = neuron.getOutput();
			i++;
		}
		return output;
	}
	
	// update networks weights with desiredOutput
	public double[] learn(double[] desiredOutput) {
		if(desiredOutput.length != outputNeurons.length) {
			throw new RuntimeException("desired output length doesn't mach network");
		}
		// backward pass
		int i = 0;
		for(DynamicNeuron neuron : outputNeurons) {
			neuron.updatePreDelta(desiredOutput[i] - neuron.getOutput());
			neuron.computeDeltaAndWeights();
			i++;
		}
		for(i=hiddenLayers.size()-1; i>=0; i--) {
			List<DynamicNeuron> layer = hiddenLayers.get(i);
			for(DynamicNeuron neuron : layer) {
				neuron.computeDeltaAndWeights();
			}
		}
		double[] preDelta = new double[inputNeurons.length];
		i = 0;
		for(InputNeuron neuron : inputNeurons) {
			preDelta[i] = neuron.preDelta();
			i++;
		}
		return preDelta;
	}
	
	void delayedLearn(double[] inputs, double[] desiredOutput) {
		evaluateGen(inputs);
		learn(desiredOutput);
	}
	
	public static void main(String[] args) {
		Network network = new Network(2, 1);
		double[][][] trainingSet = {{{0,0},{0}}, {{0,1},{0}}, {{1,0},{0}}, {{1,1},{1}}};
		for(int i=0; i<1000; i++) {
			System.out.println("i = "+i);
			for(double[][] eg : trainingSet) {
				double output = network.at(eg[0]);
				System.out.println("\tcase <"+eg[0][0]+","+eg[0][1]+
						"> output: "+output);
				network.learn(eg[1]);
			}
		}
		System.out.println("\ndone");
	}
}
