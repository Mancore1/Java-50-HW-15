package telran.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

@SuppressWarnings("unchecked")
public class ArrayList<T> implements List<T> {
	private static final int DEFAULT_CAPACITY = 16;
	private T[] array;
	private int size = 0;

	private class ArrayListIterator implements Iterator<T> {
		int currentIndex = 0;
		boolean flNext = false;

		@Override
		public boolean hasNext() {

			return currentIndex < size;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			flNext = true;
			return array[currentIndex++];
		}

		@Override
		public void remove() {
			if (!flNext) {
				throw new IllegalStateException();
			}
			ArrayList.this.remove(--currentIndex);
			flNext = false;
		}

	}

	public ArrayList(int capacity) {
		array = (T[]) new Object[capacity];
	}

	public ArrayList() {
		this(DEFAULT_CAPACITY);
	}

	@Override
	public boolean add(T obj) {
		if (size == array.length) {
			reallocate();
		}
		array[size++] = obj;
		return true;
	}

	private void reallocate() {
		array = Arrays.copyOf(array, array.length * 2);

	}

	@Override
	public boolean removeIf(Predicate<T> predicate) {
		int oldSize = size;
		int indexNum = 0;
		for (int i = 0; i < oldSize; i++) {
			if (predicate.test(array[i])) {
				size--;
			} else {
				array[indexNum++] = array[i];
			}
		}
		for (int i = size; i < oldSize; i++) {
			array[i] = null;
		}
		return oldSize > size;
	}

	@Override
	public int size() {

		return size;
	}

	@Override
	public Iterator<T> iterator() {

		return new ArrayListIterator();
	}

	@Override
	public void add(int index, T obj) {
		indexValidation(index, true);
		if (size == array.length) {
			reallocate();
		}
		System.arraycopy(array, index, array, index + 1, size - index);
		array[index] = obj;
		size++;
	}

	@Override
	public T get(int index) {
		indexValidation(index, false);
		return array[index];
	}

	@Override
	public T set(int index, T obj) {

		T res = get(index);
		array[index] = obj;
		return res;
	}

	@Override
	public T remove(int index) {
		indexValidation(index, false);
		T res = array[index];
		size--;
		System.arraycopy(array, 0, array, 0, index);
		System.arraycopy(array, index + 1, array, index, size - index);
		array[size] = null;
		return res;
	}

	@Override
	public int indexOf(Object pattern) {

		return indexOf(Predicate.isEqual(pattern));
	}

	@Override
	public int lastIndexOf(Object pattern) {

		return lastIndexOf(Predicate.isEqual(pattern));
	}

	@Override
	public int indexOf(Predicate<T> predicate) {
		int res = -1;
		int index = 0;
		while (index < size && res == -1) {
			if (predicate.test(array[index])) {
				res = index;
			}
			index++;
		}
		return res;
	}

	@Override
	public int lastIndexOf(Predicate<T> predicate) {
		int res = -1;
		int index = size - 1;
		while (index >= 0 && res == -1) {
			if (predicate.test(array[index])) {
				res = index;
			}
			index--;
		}
		return res;
	}
}
