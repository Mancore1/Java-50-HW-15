package telran.util.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import telran.util.*;

class TreeSetTest extends CollectionTest {
	TreeSet<Integer> treeSet;

	@Override
	@BeforeEach
	void setUp() {
		collection = new TreeSet<Integer>();
		super.setUp();
		treeSet = (TreeSet<Integer>) collection;
	}

	@Override
	protected void runArrayTest(Integer[] expected, Integer[] actual) {
		Integer[] expectedSorted = Arrays.copyOf(expected, expected.length);
		Integer[] actualSorted = Arrays.copyOf(actual, actual.length);
		Arrays.sort(expectedSorted);
		assertArrayEquals(expectedSorted, actualSorted);
	}

	@Override
	protected Collection<Integer> getCollection(Integer[] ar) {
		TreeSet<Integer> res = new TreeSet<>();
		for (Integer num : ar) {
			res.add(num);
		}
		return res;
	}

	@Test
	void getNodeTest() {
		assertTrue(collection.contains(numbers[0]));
		assertTrue(collection.contains(1));
		assertFalse(collection.contains(500));
		collection.clear();
		assertFalse(collection.contains(1));
	}

	@Test
	void firstTest() {
		assertEquals(Integer.valueOf(1), treeSet.first());
		collection.clear();
		assertNull(treeSet.first());
	}

	@Test
	void lastTest() {
		assertEquals(Integer.valueOf(5), treeSet.last());
		collection.clear();
		assertNull(treeSet.last());
	}

	@Test
	void ceilingTest() {
		assertEquals(Integer.valueOf(1), treeSet.ceiling(-30));
		assertEquals(Integer.valueOf(2), treeSet.ceiling(2));
		assertNull(treeSet.ceiling(200));
		collection.clear();
		assertNull(treeSet.ceiling(1));
	}

	@Test
	void floorTest() {
		assertEquals(Integer.valueOf(5), treeSet.floor(200));
		assertEquals(Integer.valueOf(4), treeSet.floor(4));
		assertNull(treeSet.floor(-30));
		collection.clear();
		assertNull(treeSet.ceiling(1));
	}
}