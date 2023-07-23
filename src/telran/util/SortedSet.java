package telran.util;

public interface SortedSet<T> extends Set<T> {
	T first(); // least element
	T last(); // greatest element
	T ceiling(T key); // least among greater than key or equals
	T floor(T key); // greatest among less than key or equal
}