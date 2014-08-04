package commons;

public interface Set<SetType extends Set<SetType>> {
	
	SetType union(Set<SetType> set);

    SetType intersection(Set<SetType> set);

    SetType complement();
}
