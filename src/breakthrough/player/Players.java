package breakthrough.player;

import breakthrough.Color;

/**
 * <p>
 * Created on 8/1/2014.
 */
public interface Players {

    /**
     * Chooses a legal move that maximizes the number advantage.
     */
    Player CountPlayer =
            new HeuristicPlayer(board -> board.count(Color.Black) - board.count(Color.White));
    /**
     * Plays randomly.
     * Chooses a legal move uniformly at random.
     */
    Player RandomPlayer = new HeuristicPlayer(board -> 0);
}
