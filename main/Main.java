package main;

import breakthrough.Color;
import breakthrough.game.Utils;
import breakthrough.heuristic.NeuralNetHeuristic;
import breakthrough.player.GreedyPlayer;
import breakthrough.player.Player;
import breakthrough.player.TD1Player;

import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 * Created on 7/27/2014.
 */
public class Main {

    public static void main(String[] args) {
        final int size = 4;
        
        playABunch(size);
    }

    private static void playOneGame(int size) {
        Player white = new TD1Player(new NeuralNetHeuristic(size));
        Player black = new GreedyPlayer();

        Utils.loggingEnabled = true;
        final Color theWinner = Utils.play(white, black, size);
        System.out.println("The winner is: " + theWinner);
    }

    private static void playABunch(int size) {
        Player white = new TD1Player(new NeuralNetHeuristic(size));
        Player black = new GreedyPlayer();

        final Map<Color, Player> player = new EnumMap<Color, Player>(Color.class);
        player.put(Color.White, white); player.put(Color.Black, black);

        final Map<Color, Integer> wins = new EnumMap<Color, Integer>(Color.class);
        wins.put(Color.White, 0); wins.put(Color.Black, 0);

        for(int i = 1; true; i++) {
            final Color winner = Utils.play(white, black, size);
            wins.put(winner, wins.get(winner) + 1);

            if(i%10000 == 0) {
                System.out.println("whiteWins: " + wins.get(Color.White) + " blackWins: " + wins.get(Color.Black));
                wins.put(Color.White, 0); wins.put(Color.Black, 0);
            }
            if(i%100000 == 0) {
                System.out.println(white);
                System.out.println(black);
            }
            if(i%1000000 == 0) {
                Utils.loggingEnabled = true;
                final Color theWinner = Utils.play(white, black, size);
                System.out.println("The winner is: " + theWinner);
                Utils.loggingEnabled = false;
            }
        }
    }
}
