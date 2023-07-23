package telran.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Predicate;

public interface Collection<T> extends Iterable<T> {
	boolean add (T obj);
	boolean remove (Object pattern);
	
	default public void clear() {
		removeIf(n -> true);
	}
	
	boolean contains(Object pattern);
	default public boolean containsAll(Collection<T> collection) {
		boolean res = true;
		Iterator<T> it = collection.iterator();
		while(it.hasNext() && res) {
			T obj = it.next();
				res = contains(obj);
			}
		return res;
	}
	
	default public T[] toArray(T[] ar) {
		int size = size();
		T[] res = ar.length < size ? Arrays.copyOf(ar, size) : ar;
		int index = 0;
		for (T obj: this) {
			res[index++] = obj;
		}
		if (res.length > size) {
			res[size] = null;
		}
		return res;
	}
	
	default public boolean removeIf(Predicate<T> predicate) {
		Iterator<T> it = iterator();
		int oldSize = size();
		while (it.hasNext()) {
			T element = it.next();
			if (predicate.test(element)) {
				it.remove();
			}
		}
		return oldSize > size();
	}
	
	int size();
	
	default public boolean addAll(Collection<T> collection) {
		int oldSize = size();
		for (T obj : collection) {
			add(obj);
		}
		return oldSize < size();
	}
	default public boolean removeAll(Collection<T> collection) {
		int oldSize = size();
		for (T obj : collection) {
				remove(obj);
		}
		return oldSize > size();
	}
}
