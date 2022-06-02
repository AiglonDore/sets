package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import sets.ArraySet;

/**
 * Test class for {@link ArraySet}
 * @author davidroussel
 */
@DisplayName("ArraySet<E> tests")
class ArraySetTest
{
	/**
	 * The set under test
	 */
	private ArraySet<String> set;

	/**
	 * Elements to fill sets :
	 * "Lorem ipsum dolor sit amet"
	 */
	private static final String[] elements1 = new String[] {
		"Lorem",
		"ipsum",
		"sit",
		"dolor",
		"amet"
	};

	/**
	 * Other elements to fill sets :
	 * "dolor amet consectetur adipisicing elit"
	 */
	private static final String[] elements2 = new String[] {
		"dolor",
		"amet",
		"consectetur",
		"adipisicing",
		"elit"
	};

	/**
	 * Elements to fill a set with duplicates in order to check they can't
	 * be added twice.
	 */
	private static final String[] elements = new String[elements1.length
			+ elements2.length];

	/**
	 * Collection to hold elements to fill sets
	 */
	private ArrayList<String> listElements;

	/**
	 * Collection to check elements in {@link #set} filled with elements of
	 * {@link #listElements} since {@link #set} won't add the same element twice
	 */
	private ArrayList<String> listUniqueElements;

	/**
	 * Complementary elements to add to {@link #set} in order to exceed
	 * {@link ArraySet} capcity and trigger growth of capacity increment
	 */
	private static final String[] elements3 = new String[] {
		"sed",
		"do",
		"eiusmod",
		"tempor",
		"incididunt",
		"ut",
		"labore",
		"et",
		"dolore",
		"magna",
		"aliqua"
	};

	/**
	 * Setup before all tests
	 */
	@BeforeAll
	static void setUpBeforeClass()
	{
		int j = 0;
		for (int i = 0; i < elements1.length; i++)
		{
			elements[j++] = elements1[i];
		}
		for (int i = 0; i < elements2.length; i++)
		{
			elements[j++] = elements2[i];
		}
		System.out.println("-------------------------------------------------");
		System.out.println("Array Sets tests");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Tear down after all tests
	 */
	@AfterAll
	static void tearDownAfterClass()
	{
		System.out.println("-------------------------------------------------");
		System.out.println("Array Sets test end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup before each test
	 */
	@BeforeEach
	void setUp()
	{
		set = new ArraySet<>();
		listElements = new ArrayList<String>();
		for (String elt : elements)
		{
			listElements.add(elt);
		}
		listUniqueElements = new ArrayList<String>();
		{
			for (String elt : elements)
			{
				if (!listUniqueElements.contains(elt))
				{
					listUniqueElements.add(elt);
				}
			}
		}
	}

	/**
	 * Tear down after each test
	 */
	@AfterEach
	void tearDown()
	{
		set.clear();
		set = null;
		listElements.clear();
		listElements = null;
		listUniqueElements.clear();
		listUniqueElements = null;
	}

	/**
	 * Test method for {@link sets.ArraySet#ArraySet()}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#capacity()}
	 */
	@Test
	@DisplayName("ArraySet<E>()")
	final void testArraySet()
	{
		String testName = new String("ArraySet()");
		System.out.println(testName);

		/*
		 * A default set has already been created in #setUp
		 */
		assertNotNull(set,
		              testName + " default set creation failed");

		/*
		 * Default created set should have
		 * size = 0
		 * capacity = sets.ArraySet#DefaultCapacity
		 */
		assertEquals(0,
		             set.size(),
		             testName + " default created set 0 size failed");
		assertEquals(sets.ArraySet.DefaultCapacity,
		             set.capacity(),
		             testName + " default created set default capacity failed");
	}

	/**
	 * Test method for {@link sets.ArraySet#ArraySet(int)}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#capacity()}
	 */
	@Test
	@DisplayName("ArraySet<E>(int initialCapacity)")
	final void testArraySetInt()
	{
		String testName = new String("ArraySet(initialCapacity)");
		System.out.println(testName);

		/*
		 * Check we can't create set with 0 capacity
		 */
		assertThrows(IllegalArgumentException.class,
		             () -> {
			             @SuppressWarnings("unused")
			             ArraySet<String> aSet = new ArraySet<>(0);
		             },
		             testName + " ArraySet<>(0) didn't throw IllegalArgumentException");
		/*
		 * A default set has already been created in #setUp so we need to change
		 * it
		 */
		int requiredCapacity = listElements.size();

		set = new ArraySet<>(requiredCapacity);
		assertNotNull(set,
		              testName + " ArraySet<>(capacity) creation failed");

		/*
		 * Capacity created set should have
		 * size = 0
		 * capacity = requiredCapacity
		 */
		assertEquals(0,
		             set.size(),
		             testName + "ArraySet<>(capacity) 0 size failed");

		assertEquals(requiredCapacity,
		             set.capacity(),
		             testName + " ArraySet<>(capacity) created set expected capacity failed");
	}

	/**
	 * Test method for {@link sets.ArraySet#ArraySet(int, int)}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#capacity()}
	 * - {@link ArraySet#add(Object)}
	 */
	@Test
	@DisplayName("ArraySet<E>(int initialCapacity, int capacityIncrement)")
	final void testArraySetIntInt()
	{
		String testName = new String("ArraySet(initialCapacity, capacityIncrement)");
		System.out.println(testName);

		/*
		 * A default set has already been created in
		 * #setUp so we need to change it
		 */
		int requiredCapacity = listElements.size();		// 10
		int requiredIncrement = requiredCapacity / 2;	// 5

		/*
		 * Check we can't create set with 0 capacity or increment
		 */
		assertThrows(IllegalArgumentException.class,
		             () -> {
			             @SuppressWarnings("unused")
			             ArraySet<String> aSet = new ArraySet<>(0, requiredIncrement);
		             },
		             testName + " ArraySet<>(0, requiredIncrement) didn't throw IllegalArgumentException");

		assertThrows(IllegalArgumentException.class,
		             () -> {
			             @SuppressWarnings("unused")
			             ArraySet<String> aSet = new ArraySet<>(requiredCapacity, 0);
		             },
		             testName + " ArraySet<>(requiredCapacity, 0) didn't throw IllegalArgumentException");

		set = new ArraySet<>(requiredCapacity, requiredIncrement);
		assertNotNull(set,
		              testName + " ArraySet<>(requiredCapacity, requiredIncrement) creation failed");

		/*
		 * Capacity, Increment created set should have
		 * size = 0
		 * capacity = requiredCapacity
		 */
		assertEquals(0,
		             set.size(),
		             testName + "ArraySet<>(requiredCapacity, requiredIncrement) 0 size failed");
		int initialCapacity = set.capacity();
		assertEquals(requiredCapacity,
		             initialCapacity,
		             testName + " ArraySet<>(requiredCapacity, requiredIncrement) created set expected capacity failed");

		/*
		 * Since ArraySet doesn't provide its capacity increment wee need to
		 * add elements until ArraySet#grow() is called to assess capacity
		 * increment
		 */
		for (String elt : listElements)
		{
			// some elts might not be added because they are already part of set
			set.add(elt);
		}

		assertEquals(listUniqueElements.size(),
		             set.size(),
		             testName + " unexpected size after fill");
		assertEquals(initialCapacity,
		             set.capacity(),
		             testName + " unexpected capacity after fill");

		int remainderUpToCapacity = set.capacity() - set.size();
		int i3 = 0;
		for (; i3 < remainderUpToCapacity; i3++)
		{
			assertTrue(set.add(elements3[i3]),
			           testName + " failed to add " + elements3[i3]);
			assertEquals(initialCapacity,
			             set.capacity(),
			             testName + " unexpected capacity after adding " + elements3[i3]);
		}

		/*
		 * Adding a new element should trigger change in capacity
		 */
		assertTrue(set.add(elements3[++i3]),
		           testName + " failed to add " + elements3[i3]);

		assertEquals(initialCapacity + requiredIncrement,
		             set.capacity(),
		             testName + " unexpected capacity after adding " + elements3[i3]);
	}

	/**
	 * Test method for {@link sets.ArraySet#ArraySet(java.util.Collection)}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#capacity()}
	 */
	@Test
	@DisplayName("ArraySet<E>(Collection<E>)")
	final void testArraySetCollectionOfQextendsE()
	{
		String testName = new String("ArraySet(Collection)");
		System.out.println(testName);

		/*
		 * A default set has already been created in
		 * #setUp so we need to change it
		 */
		set = new ArraySet<>(listElements);
		assertNotNull(set,
		              testName + " ArraySet<>(Collection) creation failed");

		/*
		 * Collection created set should have
		 * size = size of #listUniqueElements
		 * capacity = size of #listElements
		 */
		assertEquals(listUniqueElements.size(),
		             set.size(),
		             testName + "ArraySet<>(Collection) expected size failed");
		assertEquals(listElements.size(),
		             set.capacity(),
		             testName + "ArraySet<>(Collection) expected capacity failed");

		/*
		 * Augment #listElements with elts from #elements3
		 */
		listElements.addAll(Arrays.asList(elements3));
		listUniqueElements.clear();
		for (String elt : listElements)
		{
			if (!listUniqueElements.contains(elt))
			{
				listUniqueElements.add(elt);
			}
		}

		set = new ArraySet<>(listElements);

		/*
		 * Collection created set should have
		 * size = size of #listUniqueElements
		 * capacity = size of #listElements
		 */
		assertEquals(listUniqueElements.size(),
		             set.size(),
		             testName + "ArraySet<>(Collection) expected size failed");
		assertEquals(listElements.size(),
		             set.capacity(),
		             testName + "ArraySet<>(Collection) expected capacity failed");

		/*
		 * Set constructred from empty collection should have ArraySet#DefaultCapacity
		 * capacity
		 */
		List<String> emptyList = new ArrayList<>();
		set = new ArraySet<>(emptyList);
		assertEquals(ArraySet.DefaultCapacity,
		             set.capacity(),
		             testName + " unexpected capacity");
		assertEquals(0,
		             set.size(),
		             testName + " unexpected size");

	}

	/**
	 * Test method for {@link sets.ArraySet#clone()}.
	 */
	@Test
	@DisplayName("clone()")
	final void testClone()
	{
		String testName = new String("clone()");
		System.out.println(testName);

		/*
		 * A default empty set has already been created in
		 * #setUp
		 */
		assertNotNull(set, testName + " set creation failed");

		Object cloned = set.clone();

		/*
		 * The cloned set should
		 * 	- not be null
		 * 	- have the same class as the clone source
		 * 	- be distinct from clone source
		 * 	- be equal to clone source
		 */
		assertNotNull(cloned,
		              testName + " clone creation failed");
		assertEquals(ArraySet.class,
		             cloned.getClass(),
		             testName + " cloned set unexpected class "+ cloned.getClass().getSimpleName());
		assertNotSame(set,
		              cloned,
		              testName + " indistinct clone from source");
		assertEquals(set,
		             cloned,
		             testName + " clone has not same content");

		/*
		 * Same with filled set
		 */
		set  = new ArraySet<>(listUniqueElements);
		assertNotNull(set,
		              testName + " filled set creation failed");
		cloned = set.clone();
		assertNotNull(cloned,
		              testName + " filled clone creation failed");
		assertEquals(ArraySet.class,
		             cloned.getClass(),
		             testName + " filled cloned set unexpected class "+ cloned.getClass().getSimpleName());
		assertNotSame(set,
		              cloned,
		              testName + " indistinct filled clone from source");
		assertEquals(set,
		             cloned,
		             testName + " filled clone has not same content");
	}

	/**
	 * Test method for {@link sets.ArraySet#capacity()}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#add(Object)}
	 */
	@Test
	@DisplayName("capacity()")
	final void testCapacity()
	{
		String testName = new String("capacity()");
		System.out.println(testName);

		/*
		 * A default empty set has already been created in
		 * #setUp
		 */
		assertNotNull(set, testName + " set creation failed");

		/*
		 * A default constructed ArraySet should have ArraySet#DefaultCapacity
		 * capacity
		 */
		assertEquals(ArraySet.DefaultCapacity,
		             set.capacity(),
		             testName + " default constructed set doesn't have expected capacity");

		/*
		 * A capacity constructed set should have the required capacity
		 */
		int expectedCapacity = elements1.length;

		set = new ArraySet<>(expectedCapacity);
		assertNotNull(set, testName + " set with capacity creation failed");
		assertEquals(expectedCapacity,
		             set.capacity(),
		             testName + " capacity constructed set doesn't have expected capacity");

		int expectedCapacityIncrement = elements2.length;
		set = new ArraySet<>(expectedCapacity, expectedCapacityIncrement);
		assertNotNull(set, testName + " set with capacity/increment creation failed");
		assertEquals(expectedCapacity,
		             set.capacity(),
		             testName + " capacity/increment constructed set doesn't have expected capacity");

		/*
		 * While adding all elements of #elements1 capacity shouldn't change
		 */
		for (String elt : elements1)
		{
			set.add(elt);
			assertEquals(expectedCapacity,
			             set.capacity(),
			             testName + " capacity changed while adding " + elt + " element");
		}

		/*
		 * While adding all elements of #elements2, capacity should change to
		 * expectedCapacity + expectedCapacityIncrement
		 */
		for (String elt : elements2)
		{
			set.add(elt);
		}
		assertEquals(expectedCapacity + expectedCapacityIncrement,
		             set.capacity(),
		             testName + " unexpected capacity");

		/*
		 * Capacity of Collection constructed set should be the size of the
		 * collection to copy⭑ and the size should be the number of unique
		 * elements in the collection
		 * ⭑: unless the collection is empty, in such case the expected capacity
		 * should be ArraySet#DefaultCapacity
		 */
		set = new ArraySet<>(listElements);
		assertNotNull(set, testName + " set from collection creation failed");
		assertEquals(expectedCapacity + expectedCapacityIncrement,
		             set.capacity(),
		             testName + " unexpected capacity");
		assertEquals(listUniqueElements.size(),
		             set.size(),
		             testName + " unexpected size");

		List<String> emptyList = new ArrayList<>();
		set = new ArraySet<>(emptyList);
		assertNotNull(set, testName + " set from collection creation failed");
		assertEquals(ArraySet.DefaultCapacity,
		             set.capacity(),
		             testName + " unexpected capacity");
		assertEquals(0,
		             set.size(),
		             testName + " unexpected size");
	}

	/**
	 * Test method for {@link sets.ArraySet#strip()}.
	 * @implNote Dependencies:
	 * - {@link ArraySet#size()}
	 * - {@link ArraySet#capacity()}
	 */
	@Test
	@DisplayName("strip()")
	final void testStrip()
	{
		String testName = new String("capacity()");
		System.out.println(testName);

		/*
		 * A default empty set has already been created in
		 * #setUp
		 */
		assertNotNull(set, testName + " set creation failed");

		/*
		 * Check we can't strip an empty set
		 */
		assertThrows(IllegalStateException.class,
		             () -> {
		            	 set.strip();
		             },
		             testName + " successfully called on empty set");

		set  = new ArraySet<>(listElements);
		assertNotNull(set, testName + " set creation failed");

		/*
		 * Check collection constructed set have
		 * 	- capacity = collection size
		 * 	- but not necessarily size of the collection
		 */
		int currentCapacity = set.capacity();
		int currentSize = set.size();
		assertEquals(listElements.size(),
		             currentCapacity,
		             testName + " unexpected capacity");
		assertEquals(listUniqueElements.size(),
		             currentSize,
		             testName + " unexpected size");

		/*
		 * Check stripped set have same size & capacity
		 */
		set.strip();

		assertNotEquals(currentCapacity,
		                set.capacity(),
		                testName + " unexpected capacity");
		assertEquals(set.size(),
		             set.capacity(),
		             testName + " unexpected capacity");

	}
}
