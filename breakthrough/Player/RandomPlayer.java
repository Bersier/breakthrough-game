package breakthrough.player;

/**
 * Plays randomly. Chooses a legal move uniformly at random.
 * <p>
 * Created on 7/20/2014.
 */
public class RandomPlayer extends HeuristicPlayer {

    public RandomPlayer() {
        super(state -> 0);
    }
}
