package breakthrough;

final class DumbPlayer extends AbstractPlayer {
	
	DumbPlayer(Intercessor cessor) {
		super(cessor);
	}

    @Override
	public double at(Board<Move> board) {
		final Color color = Intercessor.getColorOfLastPlayer(board);
		return board.count(color) - board.count(color.opposite());
	}
}
