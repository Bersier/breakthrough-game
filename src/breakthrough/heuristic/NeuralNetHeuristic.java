package breakthrough.heuristic;

import breakthrough.Color;
import breakthrough.ValueFunction;
import breakthrough.game.Game;
import neuralNet.Network;

import static breakthrough.heuristic.Utils.*;

/**
 * Not too sure about this class. Created it so that I could use my old Network class with my
 * new framework.
 * <p>
 * Created on 7/27/2014.
 */
public class NeuralNetHeuristic implements Heuristic {

    private final ValueFunction<double[]> network;//todo Bug: as used currently, it is terribly underconfident

    public NeuralNetHeuristic(int size) {
        this.network = new Network(2*size*size, 1);
    }

    public NeuralNetHeuristic(int size, int noOfHiddenLayers) {
        this.network = new Network(2*size*size, 1, noOfHiddenLayers);
    }

    @Override
    public double at(Game state) {
        return ptoV(network.at(gameToDoubleArray(state)));
    }

    @Override
    public void learn(Game game, double value) {
        network.learn(gameToDoubleArray(game), vToP(value));
    }

    /**
     * The first half of the array saves only the positions of the black pawns,
     * the second half only the positions of the white pawns.
     * A pawn is represented by a 1, no pawn by a zero.
     *
     * @return a 1D double array representing the given game
     */
    private double[] gameToDoubleArray(Game game) {
        final int size = game.size();
        final double[] array = new double[2 * size*size];

        int k = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                final Color pawn = game.at(i, j);
                array[k]             = (pawn == Color.Black) ? 1 : 0;
                array[k + size*size] = (pawn == Color.White) ? 1 : 0;
                k++;
            }
        }

        return array;
    }

    @Override
    public String toString() {
        return "Neural net: " + network.toString();
    }
}
