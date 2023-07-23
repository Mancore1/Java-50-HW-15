package telran.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

@SuppressWarnings("unchecked")
public class TreeSet<T> implements SortedSet<T> {
	private static class Node<T> {
		T obj;
		Node<T> parent;
		Node<T> left;
		Node<T> right;

		Node(T obj) {
			this.obj = obj;
		}
	}

	Node<T> root;
	int size;
	Comparator<T> comp;

	private Node<T> getParentOrNode(T key) {
		Node<T> current = root;
		Node<T> parent = null;
		int compRes;
		while (current != null && (compRes = comp.compare(key, current.obj)) != 0) {
			parent = current;
			current = compRes < 0 ? current.left : current.right;
		}
		return current == null ? parent : current;
	}

	private Node<T> getParent(T key) {
		Node<T> node = getParentOrNode(key);
		Node<T> parent = null;
		if (node != null && comp.compare(key, node.obj) != 0) {
			parent = node;
		}
		return parent;
	}

	private Node<T> getNode(T key) {
		Node<T> node = getParentOrNode(key);
		Node<T> res = null;
		if (node != null && comp.compare(key, node.obj) == 0) {
			res = node;
		}
		return res;
	}

	public TreeSet(Comparator<T> comp) {
		this.comp = comp;
	}

	public TreeSet() {
		this((Comparator<T>) Comparator.naturalOrder());
	}

	@Override
	public T get(Object pattern) {
		Node<T> node = getNode((T) pattern);

		T res = null;
		if (node != null) {
			res = node.obj;
		}
		return res;
	}

	@Override
	public boolean add(T obj) {
		Node<T> node = new Node<T>(obj);
		boolean res = false;
		if (root == null) {
			res = true;
			root = node;
		} else {
			Node<T> parent = getParent(obj);
			if (parent != null) {
				res = true;
				node.parent = parent;
				int compRes = comp.compare(obj, parent.obj);
				if (compRes > 0) {
					parent.right = node;
				} else {
					parent.left = node;
				}
			}
		}
		if (res) {
			size++;
		}
		return res;
	}

	@Override
	public boolean remove(Object pattern) {
		Node<T> node = getParentOrNode((T) pattern);
		if (node != null && node.obj.equals(pattern)) {
			removeNode(node);
			return true;
		}
		return false;
	}

	private void removeNode(Node<T> node) {
		if (isJunction(node)) {
			removeJunctionNode(node);
		} else {
			removeNonJunctionNode(node);
		}
		size--;
	}

	private void removeJunctionNode(Node<T> node) {
		Node<T> victim = getLeastFrom(node.right);
		node.obj = victim.obj;
		removeNonJunctionNode(victim);
	}

	private void removeNonJunctionNode(Node<T> node) {
		Node<T> parentNode = node.parent;
		Node<T> child = singleChild(node);
		if (parentNode != null) {
			if (node == parentNode.right) {
				parentNode.right = child;
			} else {
				parentNode.left = child;
			}
		} else {
			root = child;
		}
		if (child != null) {
			child.parent = parentNode;
		}
	}

	private Node<T> singleChild(Node<T> node) {
		return node.right != null ? node.right : node.left;
	}

	private boolean isJunction(Node<T> node) {
		return node.left != null && node.right != null;
	}

	@Override
	public boolean contains(Object pattern) {
		return getNode((T) pattern) != null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<T> iterator() {
		return new TreeSetIterator();
	}

	private class TreeSetIterator implements Iterator<T> {
		Node<T> current = root == null ? null : getLeastFrom(root);
		Node<T> previous;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			previous = current;
			updateCurrent();
			return previous.obj;
		}

		private void updateCurrent() {
			current = current.right != null ? getLeastFrom(current.right) : getGreaterParent(current);
		}

		@Override
		public void remove() {
			if (previous == null) {
				throw new IllegalStateException();
			}
			boolean prevWasJunction = isJunction(previous);
			TreeSet.this.removeNode(previous);
			if (prevWasJunction) {
				current = previous;
			}
			previous = null;
		}
	}

	private Node<T> getGreaterParent(Node<T> node) {
		while (node.parent != null && node.parent.left != node) {
			node = node.parent;
		}
		return node.parent;
	}

	private Node<T> getLessParent(Node<T> node) {
		while (node.parent != null && node.parent.right != node) {
			node = node.parent;
		}
		return node.parent;
	}

	@Override
	public T first() {
		T res = null;
		if (root != null) {
			res = getLeastFrom(root).obj;
		}

		return res;
	}

	private Node<T> getLeastFrom(Node<T> node) {
		while (node.left != null) {
			node = node.left;
		}
		return node;
	}

	@Override
	public T last() {
		T res = null;
		if (root != null) {
			res = getGreatestFrom(root).obj;
		}

		return res;
	}

	private Node<T> getGreatestFrom(Node<T> node) {
		while (node.right != null) {
			node = node.right;
		}
		return node;
	}

	@Override
	public T ceiling(T key) {
		Node<T> node = null;
		if (root != null) {
			node = getParentOrNode(key);
			int compRes = comp.compare(key, node.obj);
			if (compRes > 0) {
				node = getGreaterParent(node);
			}
		}
		return node == null ? null : node.obj;
	}

	@Override
	public T floor(T key) {
		Node<T> node = null;
		if (root != null) {
			node = getParentOrNode(key);
			int compRes = comp.compare(key, node.obj);
			if (compRes < 0) {
				node = getLessParent(node);
			}
		}
		return node == null ? null : node.obj;
	}
}