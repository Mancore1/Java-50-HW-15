package telran.util.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.util.ArrayList;
import telran.util.Collection;

class ArrayListTest extends ListTest {
	@BeforeEach
	@Override
	void setUp() {
		collection = new ArrayList<>(3);
		super.setUp();
	}

	@Override
	protected Collection<Integer> getCollection(Integer[] ar1) {
		ArrayList<Integer> arrayList = new ArrayList<>();
		for(Integer num: ar1) {
			arrayList.add(num);
		}
		return arrayList;
	}
}
