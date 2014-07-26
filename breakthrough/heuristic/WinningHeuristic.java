package breakthrough.heuristic;

import breakthrough.game.GameState;

/**
 * <p>
 * Created on 7/26/2014.
 */
public class WinningHeuristic implements Heuristic {

    private final Heuristic els;

    public WinningHeuristic(Heuristic els) {
        this.els = els;
    }

    @Override
    public double at(GameState state) {
        return state.hasWinner() ? 1 : els.at(state);
    }
}
