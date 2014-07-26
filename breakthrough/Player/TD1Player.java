package breakthrough.player;

import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.LearningHeuristic;

/**
 * <p>
 * Created on 7/26/2014.
 */
public class TD1Player implements Player {

    private final LearningHeuristic heuristic;

    public TD1Player(LearningHeuristic heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public void gameStart(Game initialState, boolean youStart) {

    }

    @Override
    public WhiteMove play(Game current) {
        return null;
    }
}
