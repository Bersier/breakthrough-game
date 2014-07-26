package defaulter;


import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

import patterns.Pair;


public final class Triumvirate<A, B, C> {

	final Map<A, B> one = new HashMap<A, B>();
	final Map<B, C> two = new HashMap<B, C>();
	final Map<C, A> thr = new HashMap<C, A>();
	
	public boolean equals(Object o) {
		final Triumvirate<A, B, C> other = (Triumvirate<A, B, C>)o;
		for(A a : one.keySet()) {
			if(!other.containsA(a) || !getA(a).equals(other.getA(a))) {
				return false;
			}
		}
		return true;
	}
	
	public int hashCode() {
		return toSet().hashCode();
	}
	
	public boolean isEmpty() {
		return one.isEmpty();
	}
	
	public int size() {
		return one.size();
	}
	
	public Set<A> aSet() {
		return one.keySet();
	}
	
	public Set<B> bSet() {
		return two.keySet();
	}
	
	public Set<C> cSet() {
		return thr.keySet();
	}
	
	public void clear() {
		one.clear();
		two.clear();
		thr.clear();
	}
	
	public boolean containsA(A a) {
		return one.containsKey(a);
	}
	
	public boolean containsB(B b) {
		return two.containsKey(b);
	}
	
	public boolean containsC(C c) {
		return thr.containsKey(c);
	}
	
	public Set<Triple<A, B, C>> toSet() {
		final Set<Triple<A, B, C>> ans = new HashSet<Triple<A, B, C>>(one.size());
		for(Map.Entry<A, B> entry : one.entrySet()) {
			final A a = entry.getKey();
			final B b = entry.getValue();
			final C c = two.get(b);
			ans.add(new Triple<A, B, C>(a, b, c));
		}
		return ans;
	}
	
	public void put(A a, B b, C c) {
		one.put(a, b);
		two.put(b, c);
		thr.put(c, a);
	}
	
	public Pair<B, C> getA(A a) {
		final B b = one.get(a);
		final C c = two.get(b);
		return new Pair<B, C>(b, c);
	}
	
	public Pair<C, A> getB(B b) {
		final C c = two.get(b);
		final A a = thr.get(c);
		return new Pair<C, A>(c, a);
	}
	
	public Pair<A, B> getC(C c) {
		final A a = thr.get(c);
		final B b = one.get(a);
		return new Pair<A, B>(a, b);
	}
	
	private void remove(A a, B b, C c) {
		one.remove(a);
		two.remove(b);
		thr.remove(c);
	}
	
	public Pair<B, C> removeA(A a) {
		final B b = one.get(a);
		final C c = two.get(b);
		remove(a, b, c);
		return new Pair<B, C>(b, c);
	}
	
	public Pair<C, A> removeB(B b) {
		final C c = two.get(b);
		final A a = thr.get(c);
		remove(a, b, c);
		return new Pair<C, A>(c, a);
	}
	
	public Pair<A, B> removeC(C c) {
		final A a = thr.get(c);
		final B b = one.get(a);
		remove(a, b, c);
		return new Pair<A, B>(a, b);
	}
	
}
