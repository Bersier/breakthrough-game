package breakthrough;

// +random.nextGaussian()/20
// don't need to pass the color along anywhere I pass the board along!

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
	private final Board<BMove> board;
	
	Intercessor(Board<BMove> board) {
		this.board = board;
		this.size  = board.getSize();
	}

    /** @return all currently legal moves, shuffled, for the given color */
    List<BMove> getMoves(Color color) {
		return getMoves(color, board);
	}

    /** @return all legal moves, shuffled, for the given color and board */
	public static List<BMove> getMoves(Color color, Board<BMove> board) {
		final int size = board.getSize();
		final List<BMove> list = new ArrayList<BMove>(size*size / 2);
		for(int i = 0; i < size; i++) {
			for(int j = color.inc; j < size-1 + color.inc; j++) {
				for(int dir = -1; dir <= 1; dir++) {
					final BMove move = new BMove(i, j, dir);
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
    Board<BMove> board(BMove move) {
		return board(move, board);
	}

    /** @return a new board on which the given move has been executed */
	public static Board<BMove> board(BMove move, Board<BMove> board) {
		final Board<BMove> copy = board.copy();
		copy.move(move);
		return copy;
	}

    /**
     * @return a best move of the given color according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
	Max<BMove> bestMove(Color color, ValueFunction<Board<BMove>> value) {
        return bestMove(color, getMoves(color), value);
    }

    /**
     * @return a best move for the given board of the given color according to the given value function.
     * If several best moves exist, one of them is chosen randomly.
     */
	private Max<BMove> bestMove(Color color, List<BMove> list, ValueFunction<Board<BMove>> value) {
		if(list.isEmpty()) {
			throw new IllegalArgumentException("Move list is empty!");
		}
		BMove best = list.get(0);
		double bestValue = value.at(board(best));
		for(BMove move : list) {
			final double moveValue = value.at(board(move));
			if(moveValue > bestValue) {
				best = move;
				bestValue = moveValue;
			}
		}
		return new Max<BMove>(best, bestValue);
	}

    Max<BMove> problyBestMove(Color color, double epsilon, ValueFunction<Board<BMove>> value) {
        return problyBestMove(color, epsilon, getMoves(color), value);
    }

    Max<BMove> problyBestMove(Color color, ValueFunction<Board<BMove>> valueFunction) {
        return problyBestMove(color, getMoves(color), valueFunction);
    }

	private Max<BMove> problyBestMove(Color color, double epsilon,
                                      List<BMove> list, ValueFunction<Board<BMove>> value) {
		Max<BMove> best = bestMove(color, list, value);
		if(random.nextDouble() < epsilon) {
			BMove move = list.get(random.nextInt(list.size()));
			return new Max<BMove>(move, best.value);
		}
        else {
		    return best;
        }
	}

	private Max<BMove> problyBestMove(Color color, List<BMove> list,
                                      ValueFunction<Board<BMove>> value) {
		Max<BMove> best = bestMove(color, list, value);
		if(random.nextDouble() < Math.pow(1-best.value,1)/16) {
			BMove move = list.get(random.nextInt(list.size()));
			return new Max<BMove>(move, best.value);
		}
		return best;
	}

	static Color getColorOfLastPlayer(Board<BMove> board) {
		Color color = board.getTurn().opposite();
		if(color == Color.none) {
			Color winner = board.getWinner(); 
			if(winner == Color.none) {
				throw new RuntimeException("Fatal: Nobody's turn and Nobody won!");
			}
			color = winner;
		}
		return color;
	}

    /** @return a new board that represents the same game but with colors inverted */
	public static Board<BMove> reverse(Board<BMove> board) {
		final int size = board.getSize();
		Color[][] grid = new Color[size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				grid[i][j] = board.getColorAt(size - i - 1, size - j - 1).opposite();
			}
		}
		return new Breakthrough(grid, board.getTurn().opposite(), board.getWinner().opposite(),
				board.count(Color.black), board.count(Color.white));
	}

    /**
     * @return the last 'width' columns of the board, as a list, ordered lexicographically by (-j,i)
     */
	public static List<Color> getList(Board<BMove> board, int width) {
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
	public static boolean wins(Color color, Board<BMove> board) {//todo why not use getWinner function?
		if(board.count(color.opposite()) == 0) {
			return true;
		}
		final int size = board.getSize();
		final int j = (color == Color.white)? size-1 : 0;
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
		Color other = color.opposite();
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
		
		final int j = (color == Color.white)? size-1 : 0;
		for(int i=0; i<size; i++) {
			if(grid[i][j] == color) {
				return true;
			}
		}
		return false;
	}
}








