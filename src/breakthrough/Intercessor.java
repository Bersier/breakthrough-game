package breakthrough;

// +random.nextGaussian()/20
// don't need to pass the color along anywhere I pass the board along!

import commons.ValueFunction;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility object for players.
 * <p>
 * Keeps track of the game, and provides generally useful methods for players.
 * The Intercessor is trusted.
 *
 * In fact, the Intercessor really is an intercessor, between the game and the player, as, in
 * the current implementation, the player does not have direct access to the game, to prevent it
 * from cheating.
 */
public final class Intercessor {

	final static Random random = new Random();
	final int size;
	private final Board<Move> board;
	
	Intercessor(Board<Move> board) {
		this.board = board;
		this.size  = board.getSize();
	}

    /** @return all currently legal moves, shuffled, for the given color */
    List<Move> getMoves(Color color) {
		return getMoves(color, board);
	}

    /** @return all legal moves, shuffled, for the given color and board */
	public static List<Move> getMoves(Color color, Board<Move> board) {
		final int size = board.getSize();
		final List<Move> list = new ArrayList<Move>(size*size / 2);
		for(int i = 0; i < size; i++) {
			for(int j = color.inc; j < size-1 + color.inc; j++) {
				for(int dir = -1; dir <= 1; dir++) {
					final Move move = new Move(i, j, dir);
					if(board.isLegal(move)) {
						list.add(move);
					}
				}
			}
		}
		Collections.shuffle(list);
		return list;
	}

    /** @return the board resulting from the execution of the given move */
    Board<Move> board(Move move) {
		return board(move, board);
	}

    /** @return a new board on which the given move has been executed */
	public static Board<Move> board(Move move, Board<Move> board) {
		final Board<Move> copy = board.copy();
		copy.move(move);
		return copy;
	}

    /**
     * @return a best move of the given color according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
	Max<Move> bestMove(Color color, ValueFunction<Board<Move>> value) {
        return bestMove(color, getMoves(color), value);
    }

    /**
     * @return a best move for the given board of the given color according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
	private Max<Move> bestMove(Color color, List<Move> list, ValueFunction<Board<Move>> value) {
		if(list.isEmpty()) {
			throw new IllegalArgumentException("Move list is empty!");
		}
		Move best = list.get(0);
		double bestValue = value.at(board(best));
		for(Move move : list) {
			final double moveValue = value.at(board(move));
			if(moveValue > bestValue) {
				best = move;
				bestValue = moveValue;
			}
		}
		return new Max<Move>(best, bestValue);
	}

    Max<Move> problyBestMove(Color color, double epsilon, ValueFunction<Board<Move>> value) {
        return problyBestMove(color, epsilon, getMoves(color), value);
    }

    Max<Move> problyBestMove(Color color, ValueFunction<Board<Move>> valueFunction) {
        return problyBestMove(color, getMoves(color), valueFunction);
    }

	private Max<Move> problyBestMove(Color color, double epsilon,
                                      List<Move> list, ValueFunction<Board<Move>> value) {
		Max<Move> best = bestMove(color, list, value);
		if(random.nextDouble() < epsilon) {
			Move move = list.get(random.nextInt(list.size()));
			return new Max<Move>(move, best.value);
		}
        else {
		    return best;
        }
	}

	private Max<Move> problyBestMove(Color color, List<Move> list,
                                      ValueFunction<Board<Move>> value) {
		Max<Move> best = bestMove(color, list, value);
		if(random.nextDouble() < (1 - best.value)/16) {
			Move move = list.get(random.nextInt(list.size()));
			return new Max<Move>(move, best.value);
		}
		return best;
	}

	static Color getColorOfLastPlayer(Board<Move> board) {
		Color color = board.getTurn().dual();
		if(color == Color.None) {
			Color winner = board.getWinner(); 
			if(winner == Color.None) {
				throw new RuntimeException("Fatal: Nobody's turn and Nobody won!");
			}
			color = winner;
		}
		return color;
	}

    /** @return a new board that represents the same game but with colors inverted */
	public static Board<Move> reverse(Board<Move> board) {
		final int size = board.getSize();
		Color[][] grid = new Color[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid[i][j] = board.getColorAt(size - i - 1, size - j - 1).dual();
			}
		}
		return new Breakthrough(grid, board.getTurn().dual(), board.getWinner().dual(),
				board.count(Color.Black), board.count(Color.White));
	}

    /**
     * @return the last 'width' columns of the board, as a list, ordered lexicographically by (-j,i)
     */
	public static List<Color> getList(Board<Move> board, int width) {
		final int size = board.getSize();
		final List<Color> list = new ArrayList<Color>(size*width);
		for(int j = size-1; j >= Math.max(0, size-width); j--) {
			for(int i = 0; i < size; i++) {
				list.add(board.getColorAt(i, j));
			}
		}
		return list;
	}

    /** @return whether the player of the given color wins for the given board */
	public static boolean wins(Color color, Board<Move> board) {
		if(board.count(color.dual()) == 0) {
			return true;
		}
		final int size = board.getSize();
		final int j = (color == Color.White)? size-1 : 0;
		for(int i = 0; i<size; i++) {
			if(board.getColorAt(i, j) == color) {
				return true;
			}
		}
		return false;
	}

    /**
     * @return whether the player of the given color wins for the game state defined
     * by the given color matrix
     */
    public static boolean wins(Color color, Color[][] grid) {
		Color other = color.dual();
		final int size = grid.length;
		int count = 0;
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				if(grid[i][j] == other) {
					count++;
				}
			}
		}
		if(count == 0) {
			return true;
		}
		
		final int j = (color == Color.White)? size-1 : 0;
		for(int i=0; i<size; i++) {
			if(grid[i][j] == color) {
				return true;
			}
		}
		return false;
	}
}








