package breakthrough.player;

import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

/**
 * <p>
 * Created on 7/26/2014.
 */
public class TD1Player extends HeuristicPlayer {

    private Game previous = null;

    public TD1Player(Heuristic heuristic) {
        super(heuristic);
    }

    @Override
    public void gameStart(Game initialState, boolean youStart) {
        if (! youStart) {
            previous = initialState;
        }
    }

    @Override
    public WhiteMove play(Game current) {
        WhiteMove move = super.play(current);
        if (previous != null) {
            heuristic.learn(previous, lastValue);
        }
        previous = current.after(move);
        return move;
    }
}
