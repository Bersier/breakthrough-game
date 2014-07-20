package breakthrough.player;

import breakthrough.Color;
import breakthrough.Max;
import breakthrough.Move;
import breakthrough.ValueFunction;
import breakthrough.gameState.GameState;

import java.text.DecimalFormat;

/**
 * Created on 7/20/2014.
 */
public abstract class HeuristicPlayer implements Player {

    private static final DecimalFormat F = new DecimalFormat("00");
    private final ValueFunction<GameState> heuristic;
    private Color myColor;
    private Double confidence = .5;

    public HeuristicPlayer(ValueFunction<GameState> heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public void start(GameState initialState, Color yourColor) {
        start(yourColor);
    }

    @Override
    public void start(Color yourColor) {
        if (myColor != null) {
            throw new IllegalStateException("Start method got already called!");
        }
        myColor = yourColor;
    }

    @Override
    public void youWin() {}

    @Override
    public void youLoose() {}

    @Override
    public Move play(GameState current) {
        final Max<Move> bestMove = Utils.bestMove(current, myColor, heuristic);
        confidence = bestMove.value;
        return bestMove.argmax;
    }

    @Override
    public String talk() {
        return "I feel " + F.format(confidence * 100) + "% confident about winning.";
    }
}
