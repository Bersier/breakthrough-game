package breakthrough;
/*
 * pickle
 * print weights in a square
 * introduce some exploration
 */

public final class GameEngine<Move> {

	private final Board<Move>  board;
	private final Player<Move> white;
	private final Player<Move> black;
	
	/*GameEngine() {
		this(8);
	}
	GameEngine(int size) {
		board = (Board<Move>)new Breakthrough(size);
		Intercessor cessor = new Intercessor((Board<BMove>)board);
		white = (Player<Move>)new DumbPlayer(cessor);
		black = (Player<Move>)new NetPlayer(cessor);
	}*/
	GameEngine(Board<Move> board, Player<Move> white, Player<Move> black) {
		this.board = board;
		this.white = white;
		this.black = black;
	}
	
	private boolean whiteWins() {
		if(board.getWinner() == Color.white) {
			white.youWin(Color.white);
			black.youLoose(Color.black);
			whiteWins++;
			return true;
		}
		return false;
	}
	private boolean blackWins() {
		if(board.getWinner() == Color.black) {
			black.youWin(Color.black);
			white.youLoose(Color.white);
			blackWins++;
			return true;
		}
		return false;
	}
	
	private int whiteWins = 0;
	private int blackWins = 0;
	private Color play() {
		while(true) {
			Move move = white.play(Color.white);
			board.move(move);
			if(whiteWins()) {
				board.reset();
				return Color.white;
			}
			move = black.play(Color.black);
			board.move(move);
			if(blackWins()) {
				board.reset();
				return Color.black;
			}
		}
	}
	
	private Color printPlay() {
		board.printBoard();
		while(true) {
			Move move = white.printPlay(Color.white);
			board.move(move);
			board.printBoard();
			if(whiteWins()) {
				board.reset();
				return Color.white;
			}
			move = black.printPlay(Color.black);
			board.move(move);
			board.printBoard();
			if(blackWins()) {
				board.reset();
				return Color.black;
			}
		}
	}
	
	public static void main(String[] args) {
		Board<breakthrough.Move> board = new Breakthrough(4);
		Intercessor cessor = new Intercessor((Board<breakthrough.Move>)board);
		Player<breakthrough.Move> white = new NetPlayer(cessor);
		Player<breakthrough.Move> black = new NetPlayer(cessor);
		GameEngine<breakthrough.Move> game = new GameEngine<breakthrough.Move>(board, white, black);
		for(int i=1; true; i++) {
			if(i%100000 == 0) {
				System.out.println("whiteWins: "+game.whiteWins+" blackWins: "+game.blackWins);
				game.whiteWins = 0;
				game.blackWins = 0;	
			}
			if(i%1000000 == 0) {
				System.out.println(game.white);
				System.out.println(game.black);
			}
			if(i%10000000 == 0) {
				System.out.println(game.printPlay());
				System.out.println(game.printPlay());
			}
			game.play();
		}
	}
	
}
