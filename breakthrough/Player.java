package breakthrough;

interface Player<Move> {

    /**
     * Tell this player that it won.
     *
     * @param color of this player
     */
	void youWin(Color color);

    /**
     * Tell this player that it lost.
     *
     * @param color of this player
     */
	void youLoose(Color color);

    /**
     * Ask the player for a move of the given color.
     */
	Move play(Color color);
	
	Move printPlay(Color color);
	
}
