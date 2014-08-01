package breakthrough.player;

import breakthrough.Max;
import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

/**
 * Uses basic Temporal Difference to learn. Doesn't always play the best move in order to explore.
 * Does mirror learning (for now).
 * <p>
 * Created on 7/26/2014.
 */
public class TD1BPlayer extends HeuristicPlayer {

    private Game previous;

    public TD1BPlayer(Heuristic heuristic) {
        super(heuristic);
    }

    @Override
    public void gameStart(Game initialState, boolean youStart) {
        super.gameStart(initialState, youStart);
        previous = null;
    }

    @Override
    public void gameOver(boolean youWon) {
        super.gameOver(youWon);
        learn();
    }

    @Override
    public WhiteMove play(Game current) {
        final Max<WhiteMove> bestMove = Utils.usuallyBestMove(current, heuristic);

        expectedGain         = bestMove.value;
        final WhiteMove move = bestMove.argmax;

        final Game next = current.after(move);

        if (previous != null) {
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
        heuristic.learn(previous.mirror(), expectedGain);
    }
}
