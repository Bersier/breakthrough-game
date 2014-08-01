package breakthrough.heuristic;

import breakthrough.game.Game;

/**
 * Takes care of returning the right utility when the player just won.
 * Delegates to another {@link Heuristic} otherwise.
 * <p>
 * Created on 7/26/2014.
 */
public class WinningHeuristic implements Heuristic {

    private final Heuristic els;

    public WinningHeuristic(Heuristic els) {
        this.els = els;
    }

    @Override
    public double at(Game state) {
        return state.hasWinner() ? 1 : els.at(state);
    }

    @Override
    public void learn(Game state, double utility) {
        if (! state.hasWinner()) {
            els.learn(state, utility);
        }
    }
}
