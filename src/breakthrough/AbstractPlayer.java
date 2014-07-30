package breakthrough;

import commons.ValueFunction;

abstract class AbstractPlayer implements Player<Move>, ValueFunction<Board<Move>> {

	public final Intercessor cessor;
	
	public AbstractPlayer(Intercessor cessor) {
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
