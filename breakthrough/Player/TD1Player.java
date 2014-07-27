package breakthrough.player;

import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

/**
 * Uses basic Temporal Difference to learn.
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
    public void gameOver(boolean youWon) {
        heuristic.learn(previous, youWon ? 1 : -1);
    }

    @Override
    public WhiteMove play(Game current) {
        final WhiteMove move = super.play(current);
        final Game next = current.after(move);
        if (previous != null && !next.hasWinner()) {
            heuristic.learn(previous, lastValue);
        }
        previous = next;
        return move;
    }

    @Override
    public String toString() {
        return heuristic.toString();
    }
}
