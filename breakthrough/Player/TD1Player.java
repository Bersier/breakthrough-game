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

    private Game previous;

    public TD1Player(Heuristic heuristic) {
        super(heuristic);
    }

    @Override
    public void gameStart(Game initialState, boolean youStart) {
        super.gameStart(initialState, youStart);
        previous = youStart ? null : initialState;
    }

    @Override
    public void gameOver(boolean youWon) {
        super.gameOver(youWon);
        learn();
    }

    @Override
    public WhiteMove play(Game current) {
        final WhiteMove move = super.play(current);
        final Game next = current.after(move);
        if (previous != null && !next.hasWinner()) {
            learn();
        }
        previous = next;
        return move;
    }

    @Override
    public String toString() {
        return heuristic.toString();
    }

    private void learn() {
        heuristic.learn(previous, expectedGain);
    }
}
