package breakthrough.game;

import breakthrough.Color;
import breakthrough.WhiteMove;
import commons.Pair;
import commons.Thunk;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests any Game implementation. Subclassed for each actual implementation to test.
 */
@RunWith(Parameterized.class)
public abstract class GameTest {

    private static Random random = new Random();

    /**
     * This game is used as a reference. It is assumed to work correctly.
     * The behavior of testee is checked against it.
     */
    private final Game reference;

    /**
     * This game is the one being tested. It is supposed to represent the same game as reference,
     * and behave in exactly the same way.
     */
    private final Game testee;

    /**
     * The size of the games.
     */
    private final int  size;

    /**
     * JUnit calls this on each pair of arguments (represented as an array) in the collection
     * returned by the method annotated by @Parameters.
     */
    public GameTest(Game reference, Game testee) {
        this.reference = reference;
        this.testee = testee;
        this.size = reference.size();
    }

    /**
     * This is useless, but had to add it to stop JUnit from complaining.
     * The actual '@Parameters method' used is defined in subclasses of this class.
     */
    @Parameterized.Parameters
    public static Collection<Game[]> data() {
        return Arrays.asList(new Game[][]{new Game[]{getReference(2), getReference(2)}});
    }

    /**
     * @param constructor used to create new games of the game class being tested
     * @return a 'random' sample of games
     */
    static Collection<Game[]> getTestParams(Function<Game, Game> constructor) {
        return getGamesToTest(size -> constructor.apply(getReference(size)));
    }

    /**
     * @param newGame takes a size as input, and returns a Game of that size
     * @return a 'random' sample of games
     */
    private static Collection<Game[]> getGamesToTest(Function<Integer, Game> newGame) {
        return getTestGamePairs(newGame).stream()
                .map(pair -> new Game[]{ pair.first, pair.second })
                .collect(Collectors.toList());
    }

    /**
     * @param newGame takes a size as input, and returns a Game of that size
     * @return a 'random' sample of games, each in a pair where the first one is the reference,
     * and the second the testee
     */
    private static Collection<Pair<Game, Game>> getTestGamePairs(Function<Integer, Game> newGame) {
        final Collection<Pair<Game, Game>> toTest = new ArrayList<>();
        for (Integer size : new Integer[]{2, 4, 7, 8}) {
            for (int i = 0; i < 8; i++) {

                // play a 'random' game and record all the encountered boards
                Game reference = getReference(size);
                Game testee = newGame.apply(size);
                do {
                    // add each encountered board to the list
                    toTest.add(new Pair<>(testee, reference));

                    final WhiteMove move = reference.legalMoves().get(0);

                    reference = reference.after(move);
                    testee = testee.after(move);

                } while (! reference.hasWinner());

                // also add the end-game boards
                toTest.add(new Pair<>(testee, reference));
            }
        }
        return toTest;
    }

    /**
     * @return a reference starting game of the given size
     */
    private static Game getReference(Integer size) {
        return Utils.newGame(size);
    }

    /**
     * Executes the given thunk with probability 1/8.
     */
    private static void sometimes(Thunk t) {
        if (random.nextInt(8) == 0) {
            t.eval();
        }
    }

    @Test
    public void testAt() throws Exception {
        final int i = random.nextInt(size);
        final int j = random.nextInt(size);

        assertEquals(
                reference.at(i, j),
                testee.at(i, j)
        );
    }

    @Test
    public void testSize() throws Exception {
        assertEquals(
                reference.size(),
                testee.size()
        );
    }

    @Test
    public void testMirror() throws Exception {
        assertEquals(
                reference.mirror(),
                testee.mirror()
        );
    }

    @Test
    public void testIsLegal() throws Exception {
        final int i = random.nextInt(size);
        final int j = random.nextInt(size - 1);
        final WhiteMove.Direction dir = WhiteMove.Direction.values()[random.nextInt(3)];

        final WhiteMove move = new WhiteMove(i, j, dir);

        assertEquals(
                reference.isLegal(move),
                testee.isLegal(move)
        );
    }

    @Test
    public void testLegalMoves() throws Exception {
        sometimes(() -> assertEquals(
                new HashSet<WhiteMove>(reference.legalMoves()),
                new HashSet<WhiteMove>(testee.legalMoves())
        ));
    }

    @Test
    public void testFutures() throws Exception {
        sometimes(() -> assertEquals(
                new HashSet<Game>(reference.futures()),
                new HashSet<Game>(testee.futures())
        ));
    }

    @Test
    public void testHasWinner() throws Exception {
        assertEquals(
                reference.hasWinner(),
                testee.hasWinner()
        );
    }

    @Test
    public void testCount() throws Exception {
        final Color color = Color.values()[random.nextInt(3)];
        assertEquals(
                reference.count(color),
                testee.count(color)
        );
    }

    @Test
    public void testDual() throws Exception {
        sometimes(() -> assertEquals(
                reference.dual(),
                testee.dual()
        ));
    }

    @Test
    public void testEquals() throws Exception {
        assertEquals(
                reference.equals(getReference(size)),
                testee.equals(getReference(size))
        );
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(
                reference.hashCode(),
                testee.hashCode()
        );
    }
}
