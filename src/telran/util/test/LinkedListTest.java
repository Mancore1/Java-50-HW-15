package telran.util.test;

import org.junit.jupiter.api.BeforeEach;

import telran.util.Collection;
import telran.util.LinkedList;

class LinkedListTest extends ListTest {

	@BeforeEach
	@Override
	void setUp() {
		collection = new LinkedList<Integer>();
		super.setUp();
	}
	
	@Override
	protected Collection<Integer> getCollection(Integer[] ar) {
		LinkedList<Integer> linkedList = new LinkedList<>();
		for (Integer num: ar) {
			linkedList.add(num);
		}
		return linkedList;
	}
}
