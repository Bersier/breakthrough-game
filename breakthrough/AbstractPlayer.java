package breakthrough;

abstract class AbstractPlayer implements Player<BMove>, ValueFunction<Board<BMove>> {

	final Intercessor cessor;
	
	AbstractPlayer(Intercessor cessor) {
		this.cessor = cessor;
	}

	public void youWin(Color color) {}
	
	public void youLoose(Color color) {}
	
	public BMove play(Color color) {
		return cessor.bestMove(color, this).argmax;
	}
	
	public BMove printPlay(Color color) {
		return play(color);
	}
}
