package breakthrough.player;

import static java.lang.Math.sin;
import static java.lang.Math.PI;

import breakthrough.Color;
import breakthrough.ValueFunction;
import breakthrough.game.GameState;

/**
 * Greedy Player.
 * Gives first priority to winning moves, and second priority to moves with highest count advantage.
 * <p/>
 * Created on 7/23/2014.
 */
public class GreedyPlayer extends HeuristicPlayer {//todo next: composition of partial value functions?

    public GreedyPlayer() {
        super(new ValueFunction<GameState>() {
            @Override
            public double at(GameState state) {
                if (state.hasWinner()) {
                    return 1;
                }
                else {
                    final double w = state.count(Color.White);
                    final double b = state.count(Color.Black);
                    return  sin(PI/2 * (w - b)/(w + b));
                }
            }
        });
    }
}
