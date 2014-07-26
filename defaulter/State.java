package defaulter;

public interface State<Action, Obs> {

	public double act(Action a);
	
	public Obs observe();
	
}
