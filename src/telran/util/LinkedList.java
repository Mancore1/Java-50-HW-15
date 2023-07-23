package telran.util;

import java.util.Iterator;
import java.util.function.Predicate;

public class LinkedList<T> implements List<T> {
	Node<T> head;
	Node<T> tail;
	int size;

	private static class Node<T> {
		T obj;
		Node<T> prev;
		Node<T> next;

		Node(T obj) {
			this.obj = obj;
		}
	}

	private class LinkedListIterator implements Iterator<T> {
		Node<T> current = head;
		boolean flNext = false;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			T obj = current.obj;
			current = current.next;
			flNext = true;
			return obj;
		}

		public void remove() {
			if (!flNext) {
				throw new IllegalStateException();
			}
			Node<T> removedNode = current != null ? current.prev : tail;
			removeNode(removedNode);
			flNext = false;
		}
	}

	private void removeNode(Node<T> removedNode) {
		if (removedNode == head) {
			removeHead();
		} else if (removedNode == tail) {
			removeTail();
		} else {
			removeMiddle(removedNode);
		}
		size--;
	}

	private void removeHead() {
		if (head == tail) {
			head = tail = null;
		} else {
			Node<T> nextHead = head.next;
			head.obj = null;
			head.next = null;
			head = nextHead;
			nextHead.prev = null;
		}
	}

	private void removeMiddle(Node<T> removedNode) {
		Node<T> prevNode = removedNode.prev;
		Node<T> nextNode = removedNode.next;
		removedNode.prev = removedNode.next = null;
		removedNode.obj = null;
		prevNode.next = nextNode;
		nextNode.prev = prevNode;
	}

	private void removeTail() {
		Node<T> prevTail = tail.prev;
		tail.obj = null;
		tail.prev = null;
		prevTail.next = null;
		tail = prevTail;

	}

	@Override
	public Iterator<T> iterator() {
		return new LinkedListIterator();
	}

	@Override
	public boolean add(T obj) {
		Node<T> node = new Node<>(obj);
		addNode(size, node);
		return true;
	}

	private void addNode(int index, Node<T> node) {
		if (index == size) {
			addTail(node);
		} else if (index == 0) {
			addHead(node);
		} else {
			addMiddle(index, node);
		}
		size++;
	}

	private void addMiddle(int index, Node<T> node) {
		Node<T> nextNode = getNode(index);
		Node<T> prevNode = nextNode.prev;
		node.next = nextNode;
		nextNode.prev = node;
		prevNode.next = node;
		node.prev = prevNode;

	}

	private void addHead(Node<T> node) {
		node.next = head;
		head.prev = node;
		head = node;

	}

	private void addTail(Node<T> node) {
		if (tail == null) {
			head = tail = node;
		} else {
			tail.next = node;
			node.prev = tail;
		}
		tail = node;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void add(int index, T obj) {
		indexValidation(index, true);
		Node<T> node = new Node<>(obj);
		addNode(index, node);
	}

	@Override
	public T get(int index) {
		indexValidation(index, false);
		Node<T> node = getNode(index);
		return node.obj;
	}

	private Node<T> getNode(int index) {

		return index < size / 2 ? getNodeFromHead(index) : getNodeFromTail(index);
	}

	private Node<T> getNodeFromTail(int index) {
		Node<T> current = tail;
		for (int i = size - 1; i > index; i--) {
			current = current.prev;
		}
		return current;
	}

	private Node<T> getNodeFromHead(int index) {
		Node<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.next;
		}
		return current;
	}

	@Override
	public T set(int index, T obj) {
		indexValidation(index, false);
		Node<T> node = getNode(index);
		T oldValue = node.obj;
		node.obj = obj;
		return oldValue;
	}

	@Override
	public T remove(int index) {
		indexValidation(index, false);
		Node<T> node = getNode(index);
		T res = node.obj;
		removeNode(node);
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
		Node<T> current = head;
		int index = 0;
		while (current != null) {
			if (predicate.test(current.obj)) {
				return index;
			}
			current = current.next;
			index++;
		}
		return -1;
	}

	@Override
	public int lastIndexOf(Predicate<T> predicate) {
		Node<T> current = tail;
		int index = size - 1;
		while (current != null) {
			if (predicate.test(current.obj)) {
				return index;
			}
			current = current.prev;
			index--;
		}
		return -1;
	}
}