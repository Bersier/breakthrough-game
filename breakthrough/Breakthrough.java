package breakthrough;

import java.util.Arrays;
import java.util.Map;
import java.util.EnumMap;
import java.util.Stack;

/**
 * Represents a Breakthrough game in progress.
 *
 * @see breakthrough.Board
 */
public final class Breakthrough implements Board<Move> {

    /** keeps track of the state of the board */
	private Color[][] grid;

	private final int size;
	private Color turn = Color.white;

    /** keeps track of the number of pawns of each color */
	private final Map<Color, Integer> count = new EnumMap<Color, Integer>(Color.class);

	private Color winner = Color.none;

    /**
     * Creates a new game of size 8.
     */
	Breakthrough() {
		this(8);
	}

    /**
     * Creates a new game.
     *
     * @param size of the board
     */
	Breakthrough(int size) {
		this.grid = new Color[size][size];
		this.size = size;
		reset();
	}

    /**
     * Creates a new game in a state given by the arguments.
     *///todo should the two last two arguments be there?
	public Breakthrough(Color[][] board, Color turn, Color winner, int whiteCount, int blackCount) {
		this.grid = board;
		this.turn = turn;
		this.winner = winner;
		this.size = grid.length;
		this.count.put(Color.white, whiteCount);
		this.count.put(Color.black, blackCount);
	}

    /**
     * Resets the game to the start configuration.
     */
	public void reset() {//todo make this private?
		final int noOfPawnRows = 2*size / 7;

		for(int i = 0; i < size; i++) {
			for(int j = 0; j < noOfPawnRows; j++) {
				grid[i][j] = Color.white;
			}
			for(int j = noOfPawnRows; j < size - noOfPawnRows; j++) {
				grid[i][j] = Color.none;
			}
			for(int j = size - noOfPawnRows; j<size; j++) {
				grid[i][j] = Color.black;
			}
		}

		this.count.put(Color.white, size * noOfPawnRows);
		this.count.put(Color.black, size * noOfPawnRows);
		this.turn = Color.white;
		this.winner = Color.none;
	}

	public Color getColorAt(int i, int j) {
		return grid[i][j];
	}

	public Color getWinner() {
		return winner;
	}
	
	public int getSize() {
		return size;
	}
	
	public Color getTurn() {
		return turn;
	}
	
	public Color[][] getGrid() {
		Color[][] boardCopy = new Color[size][size];
		for(int i=0; i<size; i++) {
			boardCopy[i] = Arrays.copyOf(grid[i], size);
		}
		return boardCopy;
	}
	
	public Board<Move> copy() {
		return new Breakthrough(getGrid(), turn, winner,
				count.get(Color.white), count.get(Color.black));
	}
	
	private static void printHorizontalBorder(int size) {
		for(int i=0; i<size; i++) {
			System.out.print("----");
		}
		System.out.println("-");
	}
	private static void printWhite() {
		System.out.print(" O ");
	}
	private static void printBlack() {
		System.out.print(" X ");
	}
	private static void printNone() {
		System.out.print("   ");
	}
	private static void printColor(Color color) {
		if(color == Color.white) {
			printWhite();
		} else if(color == Color.black) {
			printBlack();
		} else {
			printNone();
		}
	}
	private static void printTab() {
		System.out.print("\t");
	}
	public void printBoard() {
		System.out.println("\n");
		printTab(); printHorizontalBorder(size);
		for(int i=0; i<size; i++) {
			printTab();
			for(int j=0; j<size; j++) {
				System.out.print("|");
				printColor(grid[i][j]);
			}
			System.out.println("|");
			printTab(); printHorizontalBorder(size);
		}
		System.out.println("\n");
	}
	
	public static void printStackGrid(int size, Stack<Color> stack) {
		System.out.println("\n");
		printTab(); printHorizontalBorder(size);
		for(int i=0; i<size; i++) {
			if(stack.isEmpty()) {
				break;
			}
			printTab();
			for(int j=0; j<size; j++) {
				System.out.print("|");
				printColor(stack.pop());
			}
			System.out.println("|");
			printTab(); printHorizontalBorder(size);
		}
		System.out.println("\n");
	}
	
	public void move(Move move) {
		if(!isLegal(move)) {
			throw new RuntimeException("Somebody is trying to cheat!");
		}
		final int newi = move.newi(turn);
		final int newj = move.newj(turn);
		final Color colorAtDest = grid[newi][newj]; 
		if(colorAtDest != Color.none) {
			count.put(colorAtDest, count.get(colorAtDest)-1);
		}
		moveOnGrid(move);
		updateWinner(newj);
		turn = turn.opposite();
	}
	
	private void moveOnGrid(Move move) {
		grid[move.i][move.j] = Color.none;
		grid[move.newi(turn)][move.newj(turn)] = turn;
	}
	
	public boolean isLegal(Move move) {
		if(!(move.i>=0 && move.i<size && move.j>=0 && move.j<size)) {
			throw new RuntimeException("out of bounds coordinates");
		}
		final int newj = move.newj(turn);
		if(newj<0 || newj>=size) {
			throw new RuntimeException("looking for a pawn on win row");
		}
		if(grid[move.i][move.j] != turn) {
			return false;
		}
		final int newi = move.newi(turn);
		if(newi<0 || newi>=size) {
			return false;
		}
		if(grid[newi][newj] == turn) {
			return false;
		}
		if(move.isStraight() && grid[newi][newj] != Color.none) {
			return false;
		}
		return true;
	}
	
	public int count(Color color) {
		return count.get(color);
	}

	private void updateWinner(int newj) {
		if(count.get(turn.opposite()) == 0) {
			setWinner(turn);
		} else if(turn == Color.white && newj == size-1) {
			setWinner(Color.white);
		} else if(turn == Color.black && newj == 0) {
			setWinner(Color.black);
		}
	}
	
	private void setWinner(Color color) {
		winner = color;
		turn = Color.none;
	}
	
}
