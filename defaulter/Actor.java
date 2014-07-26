package defaulter;

interface Actor<Action, Obs> {

	Action act(Obs o);
	
	void learn(double improvement);
	
}
