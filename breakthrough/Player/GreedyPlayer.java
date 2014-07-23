package breakthrough.player;

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

    public GreedyPlayer() {//todo make this return a probability
        super(new ValueFunction<GameState>() {
            @Override
            public double at(GameState state) {
                if (state.hasWinner()) {
                    return Integer.MAX_VALUE;
                }
                else {
                    return state.count(Color.White) - state.count(Color.Black);
                }
            }
        });
    }
}
