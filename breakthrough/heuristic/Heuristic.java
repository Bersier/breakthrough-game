package breakthrough.heuristic;

import breakthrough.ValueFunction;
import breakthrough.game.GameState;

/**
 * Instances of this interface represent Breakthrough heuristics.
 * <p>
 * Created on 7/26/2014.
 */
@FunctionalInterface
public interface Heuristic extends ValueFunction<GameState> {

    /**
     * -1 represents a sure loss,
     * 1 represents a sure win;
     * so the returned value should lie in the interval [-1, 1]
     *
     * @return the expected value of the given game state
     */
    @Override
    double at(GameState state);
}
