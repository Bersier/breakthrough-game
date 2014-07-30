package breakthrough;

import commons.ValueFunction;
import neuralNet.Network;

import java.text.DecimalFormat;
import java.util.EnumMap;
import java.util.Map;

// do not store! show old input to network at learn-time!
// quickprop
// rprop
// casper
// take average of past weight changes;
//if it's more than what a random walk would give,
// increase step; otherwise decrease it
// only update one weight per learning?
// learn on the symmetric of the board
// td lambda


final class NetPlayer extends AbstractPlayer {
	
	private final ValueFunction<double[]> evaluator;
	private final Map<Color, Board<Move>> old =
		new EnumMap<Color, Board<Move>>(Color.class);
	
	NetPlayer(Intercessor cessor) {
		super(cessor);
		this.evaluator = new Network(2*cessor.size*cessor.size, 1);
	}
	NetPlayer(Intercessor cessor, int noOfHiddenLayers) {
		super(cessor);
		this.evaluator = new Network(2*cessor.size*cessor.size, 1, noOfHiddenLayers);
	}
	
	private double[] input(Color color) {
		return boardToInput(color, old.get(color));
	}
	
	private double[] mirrorInput(Color color) {
		return boardToMirrorInput(color, old.get(color));
	}
	
	private void learn(Color color, double value) {
		evaluator.learn(input(color), value);
		evaluator.learn(mirrorInput(color), value);
	}
	
	public Move play(Color color) {
		final Max<Move> max = cessor.problyBestMove(color, this);
		final Board<Move> board = cessor.board(max.argmax);
		if(old.containsKey(color)) {
			learn(color, max.value);
		}
		old.put(color, board);
		return max.argmax;
	}
	
	public Move printPlay(Color color) {
		final Max<Move> max = cessor.problyBestMove(color, this);
		final Board<Move> board = cessor.board(max.argmax);
		if(old.containsKey(color)) {
			learn(color, max.value);
		}
		old.put(color, board);
		DecimalFormat f = new DecimalFormat("00");
		System.out.println("I'm "+f.format(max.value*100)+"% sure I'm winning.");
		return max.argmax;
	}
	
	public void youWin(Color color) {
		learn(color, 1.0);
		old.remove(color);
	}
	
	public void youLoose(Color color) {
		learn(color, 0.0);
		old.remove(color);
	}
	
	//sketchy
	public String toString() {
		return evaluator.toString();
	}
	
	private double[] boardToInput(Color color, Board<Move> board) {
		final int sizeSquared = cessor.size*cessor.size;
		final double[] input = new double[2*sizeSquared];
		int k = (color == Color.White) ? sizeSquared-1 : 0;
		final int kinc = (color == Color.White) ? -1 : 1;
		for(int i=cessor.size-1; i>=0; i--) {
			for(int j=cessor.size-1; j>=0; j--) {
				final Color pawn = board.getColorAt(i, j);
				if(pawn == Color.None) {
					input[k] = 0.0;
					input[sizeSquared + k] = 0.0;
				} else {
					input[k] = (1 - pawn.inc) ^ color.inc;
					input[sizeSquared + k] = pawn.inc ^ color.inc;
				}
				k += kinc;
			}
		}
		return input;
	}
	
	private double[] boardToMirrorInput(Color color, Board<Move> board) {
		final int sizeSquared = cessor.size*cessor.size;
		final double[] input = new double[2*sizeSquared];
		int k = (color == Color.White) ? sizeSquared-1 : 0;
		final int kinc = (color == Color.White) ? -1 : 1;
		for(int i=0; i<cessor.size; i++) {
			for(int j=cessor.size-1; j>=0; j--) {
				final Color pawn = board.getColorAt(i, j);
				if(pawn == Color.None) {
					input[k] = 0.0;
					input[sizeSquared + k] = 0.0;
				} else {
					input[k] = (1 - pawn.inc) ^ color.inc;
					input[sizeSquared + k] = pawn.inc ^ color.inc;
				}
				k += kinc;
			}
		}
		return input;
	}
	
	public double at(Board<Move> board) {
		final Color color = Intercessor.getColorOfLastPlayer(board);
		return evaluator.at(boardToInput(color, board));
	}
	
	public double mirrorAt(Board<Move> board) {
		final Color color = Intercessor.getColorOfLastPlayer(board);
		return evaluator.at(boardToMirrorInput(color, board));
	}
}