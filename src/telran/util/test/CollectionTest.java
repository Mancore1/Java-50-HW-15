package telran.util.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.Collection;

abstract class CollectionTest {
	Integer[] numbers = { 10, -20, 8, 14, 30, 12, 100 };
	static final int N_BIG_NUMBERS = 100_000;
	static final int N_RUNS = 1000;
	private static final int N_RUNS_CONTAINS = 100_000;
	protected Collection<Integer> collection;

	@BeforeEach
	void setUp() {
		for (Integer num : numbers) {
			collection.add(num);
		}
	}

	@Test
	void removeTest() {
		Integer[] expected1 = { -20, 8, 14, 30, 12, 100 };
		Integer[] expected2 = { -20, 8, 14, 30, 12 };
		Integer[] expected3 = { -20, 8, 30, 12 };
		assertTrue(collection.remove(10));
		runArrayTest(expected1, collection.toArray(new Integer[0]));
		assertTrue(collection.remove(100));
		runArrayTest(expected2, collection.toArray(new Integer[0]));
		assertTrue(collection.remove(14));
		runArrayTest(expected3, collection.toArray(new Integer[0]));
		assertFalse(collection.remove(100000));
		runArrayTest(expected3, collection.toArray(new Integer[0]));
	}

	@Test
	void clearTest() {
		collection.clear();
		assertEquals(0, collection.size());
	}

	@Test
	void containsTest() {
		assertTrue(collection.contains(14));
		assertFalse(collection.contains(1000000));
	}

	@Test
	void containsAllTest() {
		Integer[] ar1 = { 10, -20 };
		Integer[] ar2 = { 10, 100000 };
		Collection<Integer> col1 = getCollection(ar1);
		Collection<Integer> col2 = getCollection(ar2);
		assertTrue(collection.containsAll(col1));
		assertFalse(collection.containsAll(col2));
	}

	@Test
	void toArrayTest() {
		Integer[] ar = new Integer[1000];
		Arrays.fill(ar, 10);
		Integer[] actual = collection.toArray(ar);
		assertTrue(ar == actual);
		assertNull(actual[collection.size()]);
		runArrayTest(numbers, Arrays.copyOf(actual, collection.size()));
	}

	@Test
	void removeIfTest() {
		Integer[] expected = { 10, -20, 8, 14, 12 };
		assertTrue(collection.removeIf(num -> num >= 30));
		runArrayTest(expected, collection.toArray(new Integer[0]));
	}

	@Test
	void addAllTest() {
		Integer[] ar = { 1, 11 };
		Integer[] expected = { 10, -20, 8, 14, 30, 12, 100, 1, 11 };
		assertTrue(collection.addAll(getCollection(ar)));
		runArrayTest(expected, collection.toArray(new Integer[0]));
	}

	@Test
	void removeAllTest() {
		Integer[] ar1 = { 10, -20, 1000 };
		Collection<Integer> col1 = getCollection(ar1);
		Integer[] expected = { 8, 14, 30, 12, 100 };
		assertTrue(collection.removeAll(col1));
		assertFalse(collection.removeAll(col1));
		runArrayTest(expected, collection.toArray(new Integer[0]));
	}
	
	 @Test
	 void removeIfPerformanceTest() {
		 Integer[] bigArray = getBigArray();
		 Collection<Integer> bigCollection = null;
		 for(int i = 0; i < N_RUNS; i++) {
			 bigCollection = getCollection(bigArray);
			  bigCollection.clear();
		 }
		 assertEquals(0, bigCollection.size());
	 }
	 
	 @Test 
	 void containsPerfomanceTest() {
		 Integer[] bigArray = getBigArray();
		 Collection<Integer> bigCollection = getCollection(bigArray);
		 for (int i = 0; i < N_RUNS_CONTAINS; i++) {
			 bigCollection.contains(1000);
		 }
	 }

	private Integer[] getBigArray() {
		Integer[] res = new Integer[N_BIG_NUMBERS];
		Random gen = new Random();
		for(int i = 0; i < N_BIG_NUMBERS; i++) {
			res[i] = gen.nextInt();
		}
		return res;
	}

	protected abstract Collection<Integer> getCollection(Integer[] ar1);
	protected abstract void runArrayTest(Integer[] expected, Integer[] actual);
}