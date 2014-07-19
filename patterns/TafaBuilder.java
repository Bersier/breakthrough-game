package patterns;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

/*
 * symmetry!
 * 
 * alg:
 * have a tafa and a set of infeasibles
 * get the set of children of the infeasibles
 * remove all that are accepted by the tafa
 * remove all that are feasible and give them to the tafa
 * the remaining become the new set of infeasibles
 * repeat until the set of infeasibles is empty
 */
final class TafaBuilder {
	
	private final int size;
	private Set<Pattern> infeasibles = new HashSet<Pattern>();
	private OldTafa tafa;
	private Paluator pal;
	
	TafaBuilder() {
		this(8);
	}
	TafaBuilder(int size) {
		this.size = size;
		pal = new Paluator(size, null);
	}
	
	List<OldTafa> buildPaluator(int N) {
		final List<OldTafa> list = new ArrayList<OldTafa>(N);
		for(int i=1; i<=N; i++) {
			System.out.println("\n\nbuilding tafa for i = "+i);
			list.add(buildTafa(i));
			pal = new Paluator(size, list);
		}
		return list;
	}
	
	private OldTafa buildTafa(int N) {
		tafa = new OldTafa(size, N);
		Pattern top = new IPattern(size, N, pal);
		infeasibles.clear();
		infeasibles.add(top);
		while(!infeasibles.isEmpty()) {
			algLoop();
		}
		tafa.solidify();
		return tafa;
	}
	
	private void algLoopOld() {
		// get the set of children of the infeasibles
		System.out.println("Number of infeasibles: "+infeasibles.size());
		final Set<Pattern> children = new HashSet<Pattern>(infeasibles.size());
		for(Pattern pattern : infeasibles) {
			children.addAll(pattern.getLessers());
		}
		// remove all that are accepted by the tafa
		final List<Pattern> nonMax = new ArrayList<Pattern>(children.size()/2);
		for(Pattern pattern : children) {
			if(tafa.accepts(pattern)) {
				nonMax.add(pattern);
			}
		}
		children.removeAll(nonMax);
		// remove all that are feasible and give them to the tafa
		final Set<Pattern> feasible = new HashSet<Pattern>();
		final Set<Pattern> bah = new HashSet<Pattern>();
		for(Pattern pattern : children) {
			switch(pattern.isGood()) {
			case ya:
				feasible.add(pattern);
			break;
			case be:
				bah.add(pattern);
			break;
			}
		}
		for(Pattern pattern : feasible) {
			tafa.grow(pattern);
		}
		children.removeAll(feasible);
		children.removeAll(bah);
		// the remaining become the new set of infeasibles
		System.out.println("No of patts added: "+feasible.size());
		infeasibles = children;
	}
	private void algLoop() {
		System.out.println("Number of infeasibles: "+infeasibles.size());
		final Set<Pattern> children = new HashSet<Pattern>(infeasibles.size());
		int noOfGoodPatterns = 0;
		for(Pattern pattern : infeasibles) {
			for(Pattern lesser : pattern.getLessers()) {
				if(!children.contains(lesser) && !tafa.accepts(lesser)) {
					switch(lesser.isGood()) {
					case ya:
						noOfGoodPatterns++;
						tafa.grow(lesser);
						break;
					case no:
						children.add(lesser);
						break;
					}
				}
			}
		}
		System.out.println("No of patts added: "+noOfGoodPatterns);
		infeasibles = children;
	}
	
}
