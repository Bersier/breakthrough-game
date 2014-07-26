package defaulter;


import machineLearning.Evaluator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public final class Network implements Evaluator<double[]> {

	private final static double rate = 0.1;
	private final static Random random = new Random();
	private final List<List<DynamicNeuron>> list;
	private final InputNeuron[] inputNeurons;
	private final DynamicNeuron[] outputNeurons;
	
	public Network(int in, int out) {
		this.list = new ArrayList<List<DynamicNeuron>>();
		this.inputNeurons = new InputNeuron[in];
		for(int i=0; i<inputNeurons.length; i++) {
			inputNeurons[i] = new InputNeuron();
		}
		this.outputNeurons = new NormalNeuron[out];
		for(int i=0; i<outputNeurons.length; i++) {
			final ArrayList<Input> inputs = new ArrayList<Input>();
			//inputs.add(new Input(rate, 0.0, ConstantNeuron.singleton));
			inputs.add(new Input(rate, random.nextDouble()*2-1, ConstantNeuron.singleton));
			int ii = 0;
			for(InputNeuron neuron : inputNeurons) {
				//if(ii<16)inputs.add(new Input(rate, 1.0, neuron));
				//else inputs.add(new Input(rate, -1.0, neuron));
				//inputs.add(new Input(rate, 0.0, neuron));
				inputs.add(new Input(rate, random.nextDouble()*2-1, neuron));
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
	public void print() {
		for(DynamicNeuron neuron : outputNeurons) {
			neuron.print();
		}
	}
	
	public void delayedLearn(int inc, double desired) {
		final double[] desiredOutput = new double[1];
		desiredOutput[0] = desired;
		delayedLearn(inc, desiredOutput);
	}
	
	public double evaluate(double[] inputs) {
		double[] inputValues = new double[inputs.length];
		int i = 0;
		for(boolean input : inputs) {
			if(input) {
				inputValues[i] = 1.0;
			} else {
				inputValues[i] = 0.0;
			}
			i++;
		}
		//if(ii%100==0)System.out.println("["+inputValues[3]+"  ,"+inputValues[4]+"  ,"+inputValues[5]+"  ]");
		return evaluate((double[])inputValues)[0];
	}
	
	// evaluate network for inputValues
	//int ii=0;
	double oldOutput=Double.NaN;
	public double[] evaluate(double[] inputValues) {
		if(inputValues.length != inputNeurons.length) {
			throw new RuntimeException("input length doesn't mach network");
		}
		// forward pass
		for(int i=inputNeurons.length-1; i>=0; i--) {
			inputNeurons[i].setOutput(inputValues[i]);
		}
		for(List<DynamicNeuron> layer : list) {
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
		oldOutput = output[0];
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
		for(i=list.size()-1; i>=0; i--) {
			List<DynamicNeuron> layer = list.get(i);
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
	
	double[] delayedLearn(int inc, double[] desiredOutput) {
		if(desiredOutput.length != outputNeurons.length) {
			throw new RuntimeException("desired output length doesn't mach network");
		}
		// backward pass
		int i = 0;
		for(DynamicNeuron neuron : outputNeurons) {
			//if(ii%1000==0)System.out.println("desired: "+desiredOutput[i]+"old: "+neuron.getOldOutput());
			//ii++;
			neuron.updatePreDelta(desiredOutput[i] - neuron.getOldOutput(inc));
			neuron.computeDeltaAndWeightsDelayed(inc);
			i++;
		}
		for(i=list.size()-1; i>=0; i--) {
			List<DynamicNeuron> layer = list.get(i);
			for(DynamicNeuron neuron : layer) {
				neuron.computeDeltaAndWeightsDelayed(inc);
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
	
	public void storeOutput(int inc) {
		for(DynamicNeuron neuron : outputNeurons) {
			neuron.storeOutput(inc);
		}
		for(int i=list.size()-1; i>=0; i--) {
			List<DynamicNeuron> layer = list.get(i);
			for(DynamicNeuron neuron : layer) {
				neuron.storeOutput(inc);
			}
		}
		for(InputNeuron neuron : inputNeurons) {
			neuron.storeOutput(inc);
		}
	}
	
	public static void main(String[] args) {
		Network network = new Network(2, 1);
		double[][][] trainingSet = {{{0,0},{0}}, {{0,1},{0}}, {{1,0},{0}}, {{1,1},{1}}};
		for(int i=0; i<100; i++) {
			System.out.println("i = "+i);
			for(double[][] eg : trainingSet) {
				double output = network.evaluate(eg[0])[0];
				System.out.println("\tcase <"+eg[0][0]+","+eg[0][1]+
						"> output: "+output);
				network.learn(eg[1]);
			}
		}
		System.out.println("\ndone");
	}
	
}













