package breakthrough;

abstract class AbstractPlayer implements Player<Move>, ValueFunction<Board<Move>> {

	final Intercessor cessor;
	
	AbstractPlayer(Intercessor cessor) {
		this.cessor = cessor;
	}

	public void youWin(Color color) {}
	
	public void youLoose(Color color) {}
	
	public Move play(Color color) {
		return cessor.bestMove(color, this).argmax;
	}
	
	public Move printPlay(Color color) {
		return play(color);
	}
}
