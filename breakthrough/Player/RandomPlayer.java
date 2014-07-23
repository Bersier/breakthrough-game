package breakthrough.player;

import breakthrough.ValueFunction;
import breakthrough.gameState.GameState;

/**
 * Plays randomly. Chooses a legal move uniformly at random.
 * <p>
 * Created on 7/20/2014.
 */
public class RandomPlayer extends HeuristicPlayer {

    public RandomPlayer() {
        super(new ValueFunction<GameState>() {
            @Override
            public double at(GameState state) {
                return 0;
            }
        });
    }
}
