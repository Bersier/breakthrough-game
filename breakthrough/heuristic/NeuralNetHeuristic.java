package breakthrough.heuristic;

import breakthrough.Color;
import breakthrough.ValueFunction;
import breakthrough.game.Game;
import neuralNet.Network;

/**
 * Not too sure about this class. Created it so that I could use my old Network class with my
 * new framework.
 * <p>
 * Created on 7/27/2014.
 */
public class NeuralNetHeuristic implements Heuristic {

    private final ValueFunction<double[]> network;

    public NeuralNetHeuristic(int size) {
        this.network = new Network(2*size*size, 1);

    }

    public NeuralNetHeuristic(int size, int noOfHiddenLayers) {
        this.network = new Network(2*size*size, 1, noOfHiddenLayers);
    }

    @Override
    public double at(Game state) {
        return 2*network.at(gameToDoubleArray(state)) - 1;
    }

    @Override
    public void learn(Game game, double value) {
        network.learn(gameToDoubleArray(game), (value + 1)/2);
    }

    /**
     * The first half of the array saves only the positions of the white pawns,
     * the second half only the positions of the black pawns.
     * A pawn is represented by a 1, no pawn by a zero.
     *
     * @return a 1D double array representing the given game
     */
    private double[] gameToDoubleArray(Game game) {
        final int size = game.size();
        final int sizeSquared = size * size;
        final double[] array = new double[2 * sizeSquared];

        int k = sizeSquared-1;
        for(int i = size-1; i >= 0; i--) {
            for(int j = size-1; j >= 0; j--) {
                final Color pawn = game.at(i, j);
                if(pawn == Color.None) {
                    array[k] = 0;
                    array[sizeSquared + k] = 0;
                } else {
                    array[k] = (1 - pawn.ordinal()/2);
                    array[sizeSquared + k] = pawn.ordinal()/2;
                }
                k--;
            }
        }

        return array;
    }


    @Override
    public String toString() {
        return network.toString();
    }
}
