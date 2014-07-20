package patterns;

import breakthrough.Board;
import breakthrough.Breakthrough;
import breakthrough.Color;
import breakthrough.Intercessor;
import breakthrough.Move;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

final class IPattern implements Pattern {

	private final int size;
	private final int N;
	private final int width;
	private final Board<Move> board;
	private final Paluator pal;
	private final boolean[][] shadows;
	
	IPattern(int size, int N, Paluator pal) {
		this.size = size;
		this.N = N;
		this.width = Math.min((N+3)/2, size);
		this.pal = pal;
		final Color[][] grid = new Color[size][size];
		this.shadows = new boolean[size][width];
		int start = size - width;
		if(start == 0) {
			start = 1;
		} else if(start == 1 && N%2==0) {
			start = 2;
		}
		for(int j=0; j<start; j++) {
			for(int i=0; i<size; i++) {
				grid[i][j] = Color.none;
			}
		}
		int noOfBlacks = 0;
		for(int j=start; j<size; j++) {
			for(int i=0; i<size; i++) {
				grid[i][j] = Color.black;
				noOfBlacks++;
			}
		}
		this.board = new Breakthrough(
				grid,
				Color.none,
				Color.black,
				0,
				noOfBlacks);
		
	}
	IPattern(int size, int N, Board<Move> board,
			Paluator pal, boolean[][] shadows) {
		this.size = size;
		this.N = N;
		this.width = Math.min((N+3)/2, size);
		this.board = board;
		this.pal = pal;
		this.shadows = shadows;
	}
	
	public int hashCode() {
		List<Color> list = Intercessor.getList(board, size);
		int code = 0;
		for(Color color : list) {
			code = 3*code + (color.inc+1);
		}
		return code;
	}
	
	public Color getColorAt(int i, int j) {
		return board.getColorAt(i, j);
	}
	
	public boolean equalsOld(Object o) {
		final Pattern other = (Pattern)o;
		for(int i=0; i<size; i++) {
			for(int j=0; j<size; j++) {
				if(getColorAt(i, j) != other.getColorAt(i, j)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Color[][] getGrid() {
		return board.getGrid();
	}
	
	public boolean equals(Object o) {
		final Color[][] grid = getGrid();
		final Color[][] otherGrid = ((Pattern)o).getGrid();
		return Arrays.deepEquals(grid, otherGrid);
	}
	
	@Override
	public int count(Color color) {
		return board.count(color);
	}

	private boolean[][] shadow(int theI, int theJ) {
		theJ = theJ-size+width;
		boolean[][] newShadows = new boolean[size][width];
		for(int i=0; i<size; i++) {
			newShadows[i] = Arrays.copyOf(shadows[i], width); 
		}
		if(!shadows[theI][theJ]) {
			int k = 0;
			for(int j=theJ; j<width; j++){
				final int end = Math.min(size-1, theI+k);
				for(int i=Math.max(0, theI-k); i<=end; i++) {
					newShadows[i][j] = true;
				}
				k++;
			}
		}
		return newShadows;
	}
	
	private Board<Move> lesserAt(int i, int j) {
		final Color[][] grid = board.getGrid();
		int whites = board.count(Color.white);
		int blacks = board.count(Color.black);
		switch(grid[i][j]) {
			case black:
				if(shadows[i][j-size+width]) {
					grid[i][j] = Color.none;
				} else {
					grid[i][j] = Color.white;
					whites++;
				}
				blacks--;
			break;
			case none:
				grid[i][j] = Color.white;
				whites++;
			break;
			case white:
				return null;
		}
		final Color winner;
		final Color turn;
		final boolean whiteWins = Intercessor.wins(Color.white, grid);
		final boolean blackWins = Intercessor.wins(Color.white, grid);
		if(whiteWins) {
			winner = Color.white;
			turn = Color.none;
			if(blackWins) {
				return null;
			}
		} else if(blackWins) {
			winner = Color.black;
			turn = Color.none;
		} else {
			winner = Color.none;
			turn = (N%2==0)? Color.black : Color.white;
		}
		return new Breakthrough(grid, turn, winner,
				whites, blacks);
	}
	
	@Override
	public List<Pattern> getLessers() { // prune patterns that require too many whites!!!
		final List<Pattern> lessers = new ArrayList<Pattern>(size*width);
		for(int j=size-width; j<size; j++) {
			for(int i=0; i<size; i++) {
				Board<Move> lesser = lesserAt(i, j);
				if(lesser != null) {
					lessers.add(new IPattern(size, N, lesser, pal,
							shadow(i, j)));
				}
			}
		}
		return lessers;
	}

	@Override
	public int getN() {
		return N;
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public Color getBeginner() {
		return board.getTurn();
	}
	
	boolean isBe(Board<Move> board) {
		if(board.getWinner() == Color.white) {
			return true;
		}
		for(int i=N-2; i>0; i-=2) {
			if(pal.isWin(i, Color.white, board)) {
				//System.out.println("is be");
				return true;
			}
		}
		return false;
	}
	
	public void print() {
		board.printBoard();
	}
	
	@Override
	public Ternar isGood() {
		if(isBe(board)) {
			return Ternar.be;
		}
		if(Intercessor.wins(Color.black, board)) {
			return Ternar.no;
		}
		List<Move> moves = Intercessor.getMoves(getBeginner(), board);
		switch(getBeginner()) {
		case white:
			//System.out.println("whitein");
			for(Move move : moves) {
				Board<Move> newBoard = Intercessor.board(move, board);
				if(pal.isWin(N-1, Color.white, newBoard)) {
					//board.printBoard();
					//System.out.println("whiteout with ya");
					return Ternar.ya;
				}
			}
			return Ternar.no;
		case black:
			//System.out.println("blackin");
			if(!pal.isWin(N-1, Color.white, board)) {
				return Ternar.no;
			}
			for(Move move : moves) {
				Board<Move> newBoard = Intercessor.board(move, board);
				if(!pal.isWin(N-1, Color.white, newBoard) ||
						Intercessor.wins(Color.black, newBoard)) {
					return Ternar.no;
				}
			}
			//board.printBoard();
			//System.out.println("blackout with ya");
			return Ternar.ya;
		}
		throw new RuntimeException("unreachable code");
	}

	@Override
	public boolean match(Board<Move> board) {
		throw new RuntimeException("arg");
	}
	
	@Override
	public List<Color> getList() {
		return Intercessor.getList(board, width);
	}

}
