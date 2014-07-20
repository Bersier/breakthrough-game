package patterns;

import breakthrough.Board;
import breakthrough.Color;
import breakthrough.Intercessor;
import breakthrough.Move;
import breakthrough.ValueFunction;

import java.util.List;

// starting and non-starting patterns have to be separated!
// check whether a pattern is useful by making sure there is a way to get
// there from a board that doesn't contain a better pattern (which might be
// useless itself !?)
// but useless moves are not that useless after all
// what is the ratio between useless and useful moves???
// high if not attacker's turn
// so don't have these stored?
// have only useful moves stored for even moves!
/*
 * look at wins that can be done with n pawns instead of n turns?
 */

final class Paluator implements ValueFunction<Board<Move>> {

	private final List<OldTafa> tafas;
	private final int size;
	
	Paluator(int size, int N) {
		this.size = size;
		tafas = (new TafaBuilder(size)).buildPaluator(N);
	}
	
	Paluator(int size, List<OldTafa> tafas) {
		this.size = size;
		this.tafas = tafas;
	}
	
	boolean isWin(int N, Color color, Board<Move> board) {
		if(board.getSize() != size) {
			throw new RuntimeException("size mismatch");
		}
		if(color == Color.black) {
			board = Intercessor.reverse(board);
		}
		if(N == 0) {
			return Intercessor.wins(Color.white, board);
		} else {
			final int width = Math.min(size,(N+3)/2);
			return tafas.get(N-1).accepts(Intercessor.getList(board, width));
		}
	}
	
	@Override
	public double at(Board<Move> board) {
		// TODO
		return 0.0;
	}
	
	public static void main(String[] args) {
		new Paluator(8, 8);
		System.out.println("done");
	}
	
}
