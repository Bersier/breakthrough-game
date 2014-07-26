package breakthrough.heuristic;

import breakthrough.game.Game;
import machineLearning.LearningValueFunction;

/**
 * <p>
 * Created on 7/26/2014.
 */
public interface LearningHeuristic extends Heuristic, LearningValueFunction<Game> {}
