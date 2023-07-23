package telran.util;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class HashSet<T> implements Set<T> {
	private static final int DEFAULT_TABLE_LENGTH = 16;
	private LinkedList<T>[] hashTable;
	private float factor = 0.75f;
	private int size;

	@SuppressWarnings("unchecked")
	public HashSet(int tableLength) {
		hashTable = new LinkedList[tableLength];
	}

	public HashSet() {
		this(DEFAULT_TABLE_LENGTH);
	}

	public class HashSetIterator implements Iterator<T> {
		Integer currentIteratorIndex;
		Iterator<T> currentIterator;
		Iterator<T> prevIterator;
		boolean flNext = false;

		HashSetIterator() {
			initialstate();
		}

		private void initialstate() {
			currentIteratorIndex = getCurrentIteratorIndex(-1);
			if (currentIteratorIndex > -1) {
				currentIterator = hashTable[currentIteratorIndex].iterator();
			}
		}

		private int getCurrentIteratorIndex(int currentIndex) {
			currentIndex++;
			while (currentIndex < hashTable.length
					&& (hashTable[currentIndex] == null || hashTable[currentIndex].size() == 0)) {
				currentIndex++;
			}
			return currentIndex < hashTable.length ? currentIndex : -1;
		}

		@Override
		public boolean hasNext() {
			return currentIteratorIndex >= 0;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			T res = currentIterator.next();
			prevIterator = currentIterator;
			updateState();
			flNext = true;
			return res;
		}

		private void updateState() {
			if (!currentIterator.hasNext()) {
				currentIteratorIndex = getCurrentIteratorIndex(currentIteratorIndex);
				if (currentIteratorIndex >= 0) {
					currentIterator = hashTable[currentIteratorIndex].iterator();
				}
			}
		}

		public void remove() {
			if (!flNext) {
				throw new IllegalStateException();
			}
			prevIterator.remove();
			size--;
			flNext = false;
		}
	}

	@Override
	public Iterator<T> iterator() {
		return new HashSetIterator();
	}

	@Override
	public boolean add(T obj) {
		boolean res = false;
		if (!contains(obj)) {
			if (((float) size / hashTable.length) >= factor) {
				hashTableRectiation();
			}
			addHashTable(obj, hashTable);
			size++;
			res = true;
		}
		return res;
	}

	private void hashTableRectiation() {
		LinkedList<T>[] tmp = new LinkedList[hashTable.length * 2];
		for (LinkedList<T> list : hashTable) {
			if (list != null) {
				for (T obj : list) {
					addHashTable(obj, tmp);
				}
			}
		}
		;

		hashTable = tmp;
	}

	private void addHashTable(T obj, LinkedList<T>[] tmp) {
		int index = Math.abs(obj.hashCode() % tmp.length);
		if (tmp[index] == null) {
			tmp[index] = new LinkedList<>();
		}
		tmp[index].add(obj);
	}

	private int getIndex(Object pattern) {
		int hashCode = pattern.hashCode();
		return Math.abs(hashCode) % hashTable.length;
	}

	@Override
	public boolean remove(Object pattern) {
		int index = getIndex(pattern);
		boolean res = false;
		LinkedList<T> list = hashTable[index];
		if (list != null) {
			res = list.remove(pattern);
			if (res) {
				size--;
			}
		}
		return res;
	}

	@Override
	public boolean contains(Object pattern) {
		int index = getIndex(pattern);
		boolean res = false;
		LinkedList<T> list = hashTable[index];
		if (list != null) {
			res = list.contains(pattern);
		}
		return res;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public T get(Object pattern) {
		int index = getIndex(pattern);
		T res = null;
		LinkedList<T> list = hashTable[index];
		if (list != null) {
			Iterator<T> it = list.iterator();
			while (it.hasNext() && res == null) {
				T obj = it.next();
				if (Objects.equals(pattern, obj)) {
					res = obj;
				}
				;
			}
		}
		return res;
	}
}
