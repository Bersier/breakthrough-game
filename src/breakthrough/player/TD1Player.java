package breakthrough.player;

import breakthrough.WhiteMove;
import breakthrough.game.Game;
import breakthrough.heuristic.Heuristic;

// do not store! show old input to network at learn-time!
// quickprop
// rprop
// casper
// take average of past weight changes;
//if it's more than what a random walk would give,
// increase step; otherwise decrease it
// only update one weight per learning?
// learn on the symmetric of the board
// td lambda

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
        previous = null;
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
    }
}
