package tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import sets.AbstractSet;
import sets.ArrayListSet;
import sets.ArraySet;
import sets.Set;
import sets.SetFactory;

/**
 * Test class for all sets through {@link sets.Set} interface
 * @author davidroussel
 */
@DisplayName("Set<E>")
class SetTest
{
	/**
	 * Number of tests for repeated tests
	 */
	private static int nbTests = 1000;

	/**
	 * The set under test
	 */
	private Set<String> testSet;

	/**
	 * The type of set to test
	 */
	private Class<? extends Set<String>> testSetType;

	/**
	 * The name of the type of set to test
	 */
	private String testSetTypeName;

	/**
	 * The name of the current test in all testXXX methods
	 */
	private String testName;

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
	 * Complementary elements not present in either {@link #elements1}
	 * nor {@link #elements2}
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
	 * Elements union of {@value #elements1} and {@link #elements2}
	 */
	private static final String[] unionElements = new String[] {
		"Lorem",
		"ipsum",
		"sit",
		"dolor",
		"amet",
		"consectetur",
		"adipisicing",
		"elit"
	};

	/**
	 * Common elements of {@value #elements1} and {@link #elements2}
	 */
	private static final String[] intersectionElements = new String[] {
		"dolor",
		"amet"
	};

	/**
	 * Complement elements of {@value #elements1} and
	 * {@link #elements2}
	 */
	private static final String[] complementElements1 = new String[] {
		"Lorem",
		"ipsum",
		"sit"
	};

	/**
	 * Complement elements of {@value #elements2} and
	 * {@link #elements1}
	 */
	private static final String[] complementElements2 = new String[] {
		"consectetur",
		"adipisicing",
		"elit"
	};

	/**
	 * Non common elements to {@value #elements1} and
	 * {@link #elements2}
	 */
	private static final String[] diffElements = new String[] {
		"Lorem",
		"ipsum",
		"sit",
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
	 * Collection to hold elements to fill sets.
	 */
	private List<String> listElements;

	/**
	 * Collection containing the uniqueness of each elements
	 * in {@link #listElements}
	 */
	private List<Boolean> listElementsUniqueness;

	/**
	 * Collection to check elements in {@link #testSet} filled with elements of
	 * {@link #listElements} since {@link #testSet} won't add the same element twice
	 */
	private List<String> listUniqueElements;


	/**
	 * Different natures of sets to test
	 */
	@SuppressWarnings("unchecked")
	private static final Class<? extends Set<String>>[] setTypes =
	(Class<? extends Set<String>>[]) new Class<?>[]
	{
		ArrayListSet.class,
		ArraySet.class
	};

	/**
	 * Set class provider used for parameterized tests requiring the type of set
	 * @return a stream of Set Classes to use in each ParameterizedTest
	 */
	private static Stream<Class<? extends Set<?>>> setClassesProvider()
	{
		return Stream.of(setTypes);
	}

	/**
	 * Creates an instance of a Set<String> according to the type of set to
	 * create and eventually a content to set.
	 * @param testName the message to repeat in each assertion based on the test
	 * this method is used in
	 * @param type the type of set to create
	 * @param content the content to setup in the created set
	 * @return a new set with the required type filled with the required content
	 * (if provided)
	 */
	private static Set<String> constructSet(String testName,
	                                        Class<? extends Set<String>> type,
	                                        Collection<String> content)
	{
		Set<String> set = null;

		try
		{
			set = SetFactory.<String> getSet(type, content);
		}
		catch (SecurityException e)
		{
			fail(testName + " constructor security exception");
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " constructor not found");
		}
		catch (IllegalArgumentException e)
		{
			fail(testName + " wrong constructor arguments");
		}
		catch (InstantiationException e)
		{
			fail(testName + " instanciation exception");
		}
		catch (IllegalAccessException e)
		{
			fail(testName + " illegal access");
		}
		catch (InvocationTargetException e)
		{
			fail(testName + " invocation exception");
		}

		return set;
	}

	/**
	 * Shuffle elements from the provided elemnts array
	 * @param elements the array containing elements to shuffle
	 * @return an new array containing the same elements as the provided array
	 * with a different order
	 */
	private static String[] shuffleArray(String[] elements)
	{
		/*
		 * CAUTION elements needs to be suffled in a new list in order to
		 * preserve "elements" order
		 */
		List<String> listElements = new ArrayList<>(Arrays.asList(elements));

		Collections.shuffle(listElements);

		String[] result = new String[elements.length];
		int i = 0;
		for (String elt : listElements)
		{
			result[i++] = elt;
		}

		return result;
	}

	/**
	 * Compare elements of a set to the elements of the provided array
	 * @param testName the name of the test this method is used in
	 * @param set the set to compare with elements in the array
	 * @param array the array containing the elements to compare
	 * @return true if all elements in the array are found in the set
	 */
	private static boolean compareSet2Array(String testName,
	                                        Set<String> set,
	                                        String[] array)
	{
		for (String elt : array)
		{
			boolean contained = set.contains(elt);
			assertTrue(contained,
			           testName + " contains(" + elt + ") failed");
			if (!contained)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks that the provided set contains only one instance of each element
	 * @param <E> The type of objects in the set
	 * @param testName the name of the test this method is used in
	 * @param set the set to check
	 * @return true if the provided set contains only one instance of each element
	 */
	private static <E> boolean checkUnique(String testName, Set<E> set)
	{
		Map<E, Integer> wordCount = new HashMap<E, Integer>();
		for (E elt : set)
		{
			if (!wordCount.containsKey(elt))
			{
				wordCount.put(elt, Integer.valueOf(1));
			}
			else
			{
				Integer count = wordCount.get(elt);
				count = Integer.valueOf(count.intValue() + 1);
				wordCount.put(elt, count);
			}
		}

		for (Integer i : wordCount.values())
		{
			int countValue = i.intValue();
			assertEquals(1,
			             countValue,
			             testName + " count check #" + countValue + " failed");
			if (countValue != 1)
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Setup before all tests
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception
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
		System.out.println("Sets tests");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Tear down after al tests
	 * @throws java.lang.Exception if teardown fails
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception
	{
		System.out.println("-------------------------------------------------");
		System.out.println("Sets test end");
		System.out.println("-------------------------------------------------");
	}

	/**
	 * Setup variables for a specific test.
	 * To be used in every testXXX(...) methods requiring Sets<String> instances.
	 * @param set the set to test
	 * @param testName the name of the current test
	 */
	@SuppressWarnings("unchecked")
	void setUpTest(Set<String> set, String testName)
	{
		testSet = set;
		testSetType = (Class<? extends Set<String>>) testSet.getClass();
		testSetTypeName = testSetType.getSimpleName();
		this.testName = testSetTypeName + "." + testName;
		System.out.println(this.testName);
	}

	/**
	 * Setup before each test
	 * @throws java.lang.Exception if setup fails
	 */
	@BeforeEach
	void setUp() throws Exception
	{
		listElements = new ArrayList<String>();
		listElementsUniqueness = new ArrayList<Boolean>();
		for (String elt : elements)
		{
			if (listElements.contains(elt))
			{
				listElementsUniqueness.add(false);
			}
			else
			{
				listElementsUniqueness.add(true);
			}
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
	 * Teardown after each test
	 * @throws java.lang.Exception if teardown fails
	 */
	@AfterEach
	void tearDown() throws Exception
	{
		testSet = null;
		listElements.clear();
		listElements = null;
		listElementsUniqueness.clear();
		listElementsUniqueness = null;
		listUniqueElements.clear();
		listUniqueElements = null;
	}

	/**
	 * Test method for all sets constructors such as {@link sets.ArraySet#ArraySet()}
	 * @param type the type of set provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#isEmpty()}
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("Default Constructor")
	final void testDefaultConstructor(Class<? extends Set<String>> type)
	{
		String testName = new String(type.getSimpleName() + "()");
		System.out.println(testName);

		Constructor<? extends Set<String>> defaultConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[0];

		try
		{
			defaultConstructor = type.getConstructor(constructorsArgs);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " constructor not found");
		}
		catch (SecurityException e)
		{
			fail(testName + " constructor security exception");
		}

		if (defaultConstructor != null)
		{
			Object instance = null;
			Object[] args = new Object[0];
			try
			{
				instance = defaultConstructor.newInstance(args);
			}
			catch (InstantiationException e)
			{
				fail(testName + " instanciation exception : Abstract class");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " constructor is inaccessible");
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " illegal argument");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invoked constructor throwed an exception");
			}

			assertNotNull(instance,
			              testName + " null instance");
			assertEquals(type,
			             instance.getClass(),
			             testName + " unexpected instance class");
			@SuppressWarnings("unchecked")
			Set<String> set = (Set<String>) instance;
			assertTrue(set.isEmpty(),
			           testName + " unexpected non empty on default instance");
			assertEquals(0,
			             set.size(),
			             testName + " unexpected non 0 size on default instance");
		}
		else
		{
			fail(testName + " null constructor");
		}
	}
	/**
	 * Test method for all sets constructors such as {@link sets.ArraySet#ArraySet(Collection)}
	 * @param type the type of set provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#isEmpty()}
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("Copy Constructor")
	final void testCopyConstructor(Class<? extends Set<String>> type)
	{
		String testName = new String(type.getSimpleName() + "(Collection)");
		System.out.println(testName);

		Constructor<? extends Set<String>> copyConstructor = null;
		Class<?>[] constructorsArgs = new Class<?>[1];
		constructorsArgs[0] = Collection.class;

		try
		{
			copyConstructor = type.getConstructor(constructorsArgs);
		}
		catch (NoSuchMethodException e)
		{
			fail(testName + " copy constructor not found");
		}
		catch (SecurityException e)
		{
			fail(testName + " copy constructor security exception");
		}

		if (copyConstructor != null)
		{
			Object instance = null;
			Object[] args = new Object[1];
			args[0] = listElements;
			try
			{
				instance = copyConstructor.newInstance(args);
			}
			catch (InstantiationException e)
			{
				fail(testName + " copy instanciation exception : Abstract class");
			}
			catch (IllegalAccessException e)
			{
				fail(testName + " copy constructor is inaccessible");
			}
			catch (IllegalArgumentException e)
			{
				fail(testName + " copy constructor illegal argument");
			}
			catch (InvocationTargetException e)
			{
				fail(testName + " invoked copy constructor throwed an exception");
			}
			catch (Exception e)
			{
				fail(testName + " setup failed");
			}

			assertNotNull(instance, testName + " null instance");
			assertEquals(type,
			             instance.getClass(),
			             testName + " unexpected instance class");
			@SuppressWarnings("unchecked")
			Set<String> set = (Set<String>) instance;
			assertFalse(set.isEmpty(),
			            testName + " unexpected non empty on copy instance");
			assertEquals(listUniqueElements.size(),
			             set.size(),
			             testName + " unexpected non size on copy instance");
			// assert expected elements
			assertTrue(compareSet2Array(testName, set, unionElements),
			           testName + " all expected elements are not contained");
		}
		else
		{
			fail(testName + " null constructor");
		}
	}

	/**
	 * Test method for {@link sets.Set#size()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("size()")
	final void testSize(Class<? extends Set<String>> type)
	{
		String baseTestName = "size()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertEquals(0,
		             testSet.size(),
		             testName + " size 0 on empty set failed");

		testSet = constructSet(testName, testSetType, listElements);
		assertNotNull(testSet,
		              testName + " non null instance failed");

		assertEquals(unionElements.length,
		             testSet.size(),
		             testName + " size " + unionElements.length
		 		    + " on filled set failed");

		/*
		 * The implementation of ArraySet#size is supposed to be much faster
		 * than Set#size, so we'll test it :
		 */
		long currentDuration = 0;
		long superDuration = 0;
		long start;
		AbstractSet<String> abstractTestSet = (AbstractSet<String>) testSet;
		for (int i = 0; i < nbTests; i++)
		{
			start = System.nanoTime();
			int cSize = testSet.size();
			currentDuration += System.nanoTime() - start;

			start = System.nanoTime();
			int sSize = abstractTestSet.superSize();
			superDuration += System.nanoTime() - start;

			assertEquals(sSize,
			             cSize,
			             testName + " size() & super.size() have different results");
		}

		double currentMeanDuration = currentDuration / (double)nbTests;
		double superMeanDuration = superDuration / (double)nbTests;

		System.out.println("\t" + testName + " = " + currentMeanDuration
		    + " ns, Set." + baseTestName + " = " + superMeanDuration
		    + " ns");

		assertTrue(currentMeanDuration < superMeanDuration,
		           testName + " implementation in class " + testSetTypeName
		               + " (" + currentMeanDuration
		               + " ns) is not faster than implementation in Set ("
		               + superMeanDuration + " ns)");
	}

	/**
	 * Test method for {@link sets.Set#isEmpty()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("isEmpty()")
	final void testIsEmpty(Class<? extends Set<String>> type)
	{
		String baseTestName = "isEmpty()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertTrue(testSet.isEmpty(),
		           testName + " empty set failed");

		testSet = constructSet(testName, testSetType, listElements);
		assertNotNull(testSet,
		              testName + " non null instance failed");

		assertFalse(testSet.isEmpty(),
		            testName + " non empty set failed");

		/*
		 * The implementation of ArraySet#isEmpty is supposed to be much faster
		 * than Set#isEmpty, so we'll test it :
		 */
		long currentDuration = 0;
		long superDuration = 0;
		long start;
		AbstractSet<String> abstractTestSet = (AbstractSet<String>) testSet;
		for (int i = 0; i < nbTests; i++)
		{
			start = System.nanoTime();
			boolean cEmpty = testSet.isEmpty();
			currentDuration += System.nanoTime() - start;

			start = System.nanoTime();
			boolean sEmpty = abstractTestSet.superIsEmpty();
			superDuration += System.nanoTime() - start;

			assertEquals(sEmpty,
			             cEmpty,
			             testName + " different result between isEmpty() & super.isEmpty()");
		}

		double currentMeanDuration = currentDuration / (double)nbTests;
		double superMeanDuration = superDuration / (double)nbTests;

		System.out.println("\t" + testName + " = " + currentMeanDuration
		       		    + " ns, Set." + baseTestName + " = " + superMeanDuration
		       		    + " ns");

		assertTrue(currentMeanDuration < superMeanDuration,
		           testName + " implementation in class " + testSetTypeName
		               + " (" + currentMeanDuration
		               + " ns) is not faster than implementation in Set ("
		               + superMeanDuration + " ns)");
	}

	/**
	 * Test method for {@link sets.Set#clear()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#isEmpty()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("clear()")
	final void testClear(Class<? extends Set<String>> type)
	{
		String baseTestName = "clear()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertTrue(testSet.isEmpty(),
		           testName + " empty set before clear failed");

		testSet.clear();

		assertTrue(testSet.isEmpty(),
		           testName + " empty set after clear failed");

		testSet = constructSet(baseTestName, type, listElements);
		assertNotNull(testSet,
		              testName + " non null full instance failed");

		assertFalse(testSet.isEmpty(),
			           testName + " non empty set before clear failed");

		testSet.clear();

		assertTrue(testSet.isEmpty(),
		           testName + " empty set after clear failed");

		/*
		 * The implementation of ArraySet#clear is supposed to be much faster
		 * than Set#clear, so we'll test it :
		 */
		long currentDuration = 0;
		long superDuration = 0;
		long start;
		AbstractSet<String> abstractTestSet = (AbstractSet<String>) testSet;
		for (int i = 0; i < nbTests; i++)
		{
			testSet = constructSet(baseTestName, type, listElements);
			assertNotNull(testSet,
			              testName + " non null empty instance failed");

			start = System.nanoTime();
			testSet.clear();
			currentDuration += System.nanoTime() - start;
			assertTrue(testSet.isEmpty(),
			           testName + " empty set after clear failed");


			// We don't need to assess results since clear is void
			// But we need to re-fill the set before clearing again
			testSet = constructSet(baseTestName, type, listElements);
			assertNotNull(testSet,
			              testName + " non null empty instance failed");
			abstractTestSet = (AbstractSet<String>)testSet;

			start = System.nanoTime();
			abstractTestSet.superClear();
			superDuration += System.nanoTime() - start;
			assertTrue(abstractTestSet.isEmpty(),
			           testName + " empty set after super clear failed");
		}

		double currentMeanDuration = currentDuration / (double)nbTests;
		double superMeanDuration = superDuration / (double)nbTests;

		System.out.println("\t" + testName + " = " + currentMeanDuration
		       		    + " ns, Set." + baseTestName + " = " + superMeanDuration
		       		    + " ns");

		assertTrue(currentMeanDuration < superMeanDuration,
		           testName + " implementation in class " + testSetTypeName
		               + " (" + currentMeanDuration
		               + " ns) is not faster than implementation in Set ("
		               + superMeanDuration + " ns)");
	}

	/**
	 * Test method for {@link sets.Set#hashCode()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("hashcode()")
	final void testHashCode(Class<? extends Set<String>> type)
	{
		String baseTestName = "hashcode()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");

		/*
		 * Expected hash of empty set is 0
		 */
		int expectedHash = 0;
		assertEquals(expectedHash,
		             testSet.hashCode(),
		             testName + " hashcode for empty set failed");
		// fill set
		testSet = constructSet(baseTestName, type, listElements);
		int elementsHashCode = 0;
		for (String elt : listElements)
		{
			elementsHashCode += elt.hashCode();
		}
		int uniqueElementsHashCode = 0;
		for (String elt : listUniqueElements)
		{
			uniqueElementsHashCode += elt.hashCode();
		}
		int hash = testSet.hashCode();
		assertNotEquals(elementsHashCode,
		                hash,
		                testName + " set hashcode == no single elts hashcode");

		assertEquals(uniqueElementsHashCode,
		             hash,
		             testName + " set hashcode == single elts hashcode failed");

		/*
		 * Check that hashcode doesn't depend on elements order
		 */
		String[] shuffledElements = shuffleArray(elements);
		listElements.clear();
		for (int i = 0; i < shuffledElements.length; i++)
		{
			listElements.add(shuffledElements[i]);
		}
		testSet = constructSet(baseTestName, type, listElements);
		assertEquals(hash,
		             testSet.hashCode(),
		             testName + " hashcode changed based on elements order");
	}

	/**
	 * Test method for {@link sets.Set#add(java.lang.Object)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("add(E)")
	final void testAddE(Class<? extends Set<String>> type)
	{
		String baseTestName = "add(E)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		/*
		 * Add elements from #listElements while checking the result
		 * with #listElementsUniqueness
		 */
		Iterator<String> wordIterator = listElements.iterator();
		Iterator<Boolean> uniquenessIterator = listElementsUniqueness.iterator();
		while (wordIterator.hasNext() && uniquenessIterator.hasNext())
		{
			boolean added = uniquenessIterator.next();
			String addedWord = wordIterator.next();
			assertEquals(added,
			             testSet.add(addedWord),
			             testName + addedWord + " added multiple times");
		}

		/*
		 * Check the resulting set doesn't contains duplicate elements
		 * Should not be necessary based on the test above
		 */
		assertTrue(SetTest.<String>checkUnique(testName, testSet),
		           testName + " duplicates elements found");

		/*
		 * Compare testSet elements with expected elements
		 */
		assertTrue(compareSet2Array(testName, testSet, unionElements),
		           testName + " all expected elements are not contained");

		/*
		 * Check we can't add null elements
		 */
		assertThrows(NullPointerException.class, () -> {
						testSet.add(null);
					 },
		             testName + " successfully added null element");
	}

	/**
	 * Test method for {@link sets.Set#addAll(java.util.Collection)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("addAll(Collection)")
	final void testAddAllCollectionOfQextendsE(Class<? extends Set<String>> type)
	{
		String baseTestName = "addAll(Collection)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		/*
		 * Add elements from #listElements and check the resulting
		 * set has changed
		 */
		assertTrue(testSet.addAll(listElements),
		           testName + " set not changed after adding collection");

		/*
		 * Check the resulting set doesn't contains duplicate elements
		 * Should not be necessary based on the test above
		 */
		assertTrue(SetTest.<String>checkUnique(testName, testSet),
		           testName + " duplicates elements found");

		/*
		 * Compare testSet elements with expected elements
		 */
		assertTrue(compareSet2Array(testName, testSet, unionElements),
		           testName + " some expected elements are not contained");

		/*
		 * Check we can't add null collections
		 */
		assertThrows(NullPointerException.class, () -> {
						testSet.addAll(null);
					 },
		             testName + " successfully added null collection");

		/*
		 * Check we can't add collection containing null elts (will throw)
		 */
		listElements.add(null);
		assertTrue(listElements.contains(null),
		           testName + " failed to add null elt into listElements");
		assertThrows(NullPointerException.class, () -> {
						testSet.addAll(listElements);
					 },
		             testName + " successfully added collection containing null element");
	}

	/**
	 * Test method for {@link sets.Set#contains(java.lang.Object)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("contains(Object)")
	final void testContainsObject(Class<? extends Set<String>> type)
	{
		String baseTestName = "contains(Object)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		/*
		 * Check set can't contain null on empty set
		 */
		assertDoesNotThrow(() -> { testSet.contains(null); },
		                   testName + " contains(null) on empty set throwed an exception");
		assertFalse(testSet.contains(null),
		            testName + " contains(null) on empty set returned true");

		/*
		 * Check expected elements are all contained
		 */
		testSet = constructSet(testName, type, listElements);
		for (String elt : listElements)
		{
			assertTrue(testSet.contains(elt),
			           testName + " element " + elt + " was not contained");
		}

		/*
		 * Check unexpected elements are not contained
		 */
		for (String elt : elements3)
		{
			assertFalse(testSet.contains(elt),
			            testName + " element " + elt + " was contained");
		}

		/*
		 * Check set can't contain null on filled set
		 */
		assertDoesNotThrow(() -> { testSet.contains(null); },
		                   testName + " contains(null) on filled set throwed an exception");
		assertFalse(testSet.contains(null),
		            testName + " contains(null) on filled set returned true");

		/*
		 * The implementation of ArraySet#contains and ArrayListSet#contains is
		 * supposed to be much faster than Set#contains, so we'll test it :
		 */
		long currentDuration = 0;
		long superDuration = 0;
		long start;
		List<String> shuffledElements = new ArrayList<>(Arrays.asList(shuffleArray(elements)));
		AbstractSet<String> abstractTestSet = (AbstractSet<String>) testSet;
		for (int i = 0; i < nbTests; i++)
		{
			for (String elt : shuffledElements)
			{
				start = System.nanoTime();
				boolean cEmpty = testSet.contains(elt);
				currentDuration += System.nanoTime() - start;

				start = System.nanoTime();
				boolean sEmpty = abstractTestSet.superContains(elt);
				superDuration += System.nanoTime() - start;

				assertEquals(sEmpty,
				             cEmpty,
				             testName + " different result between contains(Object) & super.contains(object)");
			}
		}

		double currentMeanDuration = currentDuration / (double)nbTests;
		double superMeanDuration = superDuration / (double)nbTests;

		System.out.println("\t" + testName + " = " + currentMeanDuration
		       		    + " ns, Set." + baseTestName + " = " + superMeanDuration
		       		    + " ns");

		assertTrue(currentMeanDuration < superMeanDuration,
		           testName + " implementation in class " + testSetTypeName
		               + " (" + currentMeanDuration
		               + " ns) is not faster than implementation in Set ("
		               + superMeanDuration + " ns)");
	}

	/**
	 * Test method for {@link sets.Set#containsAll(java.util.Collection)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 * - {@link Set#contains(Object)}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("containsAll(Collection)")
	final void testContainsAllCollectionOfQ(Class<? extends Set<String>> type)
	{
		String baseTestName = "containsAll(Collection)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		/*
		 * Check set can't contain null on empty set
		 */
		assertThrows(NullPointerException.class,
		             () -> {testSet.containsAll(null);},
		             testName + " containsAll(null) on empty set didn't throw exception");

		/*
		 * Checks an empty set contains an empty collection
		 */
		assertTrue(testSet.containsAll(new ArrayList<>()),
		           testName + " empty set does not contains all elements of an empty collection");

		/*
		 * Check expected elements are all contained
		 */
		testSet = constructSet(testName, type, listElements);
		assertTrue(testSet.containsAll(listUniqueElements),
		           testName + " failed to contain all expected elements (without duplicates)");
		assertTrue(testSet.containsAll(listElements),
		           testName + " failed to contain all expected elements (with duplicates)");

		/*
		 * Check expected elements in different order ar all contained
		 */
		List<String> otherListElements = Arrays.asList(shuffleArray(elements));
		assertTrue(testSet.containsAll(otherListElements),
		           testName + " failed to contain all expected elements (in different order)");

		/*
		 * Augment all elements in order not to be all contained
		 */
		listElements.add(elements3[0]);
		assertFalse(testSet.containsAll(listElements),
		            testName + " contained all elements + extra element");
		listElements.remove(elements3[0]);

		/*
		 * Check all elements in reduced #listUniqueElements are still all contained
		 */
		for (Iterator<String> it = listUniqueElements.iterator(); it.hasNext();)
		{
			assertTrue(testSet.containsAll(listUniqueElements),
			           testName + "failed to contain all of " + listUniqueElements);
			it.next();
			it.remove();
		}

		/*
		 * Check set can't contain null on filled set
		 */
		assertThrows(NullPointerException.class,
		             () -> {testSet.containsAll(null);},
		             testName + " containsAll(null) on filled set didn't throw exception");
	}


	/**
	 * Test method for {@link sets.Set#iterator()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link ArraySet#ArraySetIterator}
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("iterator()")
	final void testIterator(Class<? extends Set<String>> type)
	{
		String baseTestName = "iterator()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		Iterator<String> setIt = testSet.iterator();
		assertNotNull(setIt,
		              testName +  " non null iterator instance failed");

		/*
		 * Check iterator has nothing to iterate on empty set
		 */
		assertFalse(setIt.hasNext(),
		            testName + " !hasNext() on empty set failed");

		assertThrows(NoSuchElementException.class,
		             () -> {
		            	 Iterator<String> it = testSet.iterator();
		            	 it.next();
		             },
		             testName + "it.next() successfully called on empty set");
		assertThrows(IllegalStateException.class,
		             () -> {
		            	 Iterator<String> it = testSet.iterator();
		            	 it.remove();
		             },
		             testName + "it.remove() successfully called on empty set");

		/*
		 * Iterations on filled set
		 */
		testSet = constructSet(testName, type, listElements);
		setIt = testSet.iterator();

		assertTrue(setIt.hasNext(),
		           testName + " hasNext() failed on filled set");

		Iterator<String> listIt = listUniqueElements.iterator();
		while (setIt.hasNext() && listIt.hasNext())
		{
			String expected = listIt.next();
			String provided = null;
			try
			{
				provided = setIt.next();
			}
			catch (NoSuchElementException e)
			{
				fail(testName + " NoSuchElementException catched during next() call");
			}
			assertEquals(expected,
			             provided,
			             testName + " expected element = " + expected + " got "
			                 + provided);
		}

		/*
		 * Check iterator is finished
		 */
		assertEquals(listIt.hasNext(),
		             setIt.hasNext(),
		             testName + " unexpected hasNext state at the end of iteration");

   	 	try
		{
   			setIt.next();
   			fail(testName + " call to next() after last element succeeded");
		}
		catch (NoSuchElementException e)
		{
			// DO NOTHING: expected
		}

		/*
		 * remove test in order
		 */
		setIt = testSet.iterator();
		listIt = listUniqueElements.iterator();
		int setSize = testSet.size();
		while (listIt.hasNext() && setIt.hasNext())
		{
			String toRemove = listIt.next();
			String current = setIt.next();
			assertEquals(toRemove,
			             current,
			             testName + " expected element = " + toRemove + " got "
			                 + current);
			try
			{
				setIt.remove();
			}
			catch (IllegalStateException e)
			{
				fail(testName + " catched IllegalStateException while calling it.remove()");
			}
			for (String elt : testSet)
			{
				assertFalse(elt.equals(toRemove),
				            testName + " found element " + toRemove
				                + " after it.remove()");
			}
			assertEquals(--setSize,
			             testSet.size(),
			             testName + " unexpected set size after it.remove()");
		}

		assertEquals(listIt.hasNext(),
		             setIt.hasNext(),
		             testName + " unexpected hasNext() value at the end of "
		             	+ "iteration (with remove)");
		/*
		 * remove test in different order
		 */
		testSet = constructSet(testName, type, listElements);
		List<String> shuffledListUniqueElements = new ArrayList<>(Arrays.asList(shuffleArray(listUniqueElements.toArray(new String[listUniqueElements.size()]))));
		setIt = testSet.iterator();
		listIt = shuffledListUniqueElements.iterator();
		setSize = testSet.size();
		String toRemove = listIt.next();
		while (testSet.size() > 0)
		{
			if (!setIt.hasNext())
			{
				setIt = testSet.iterator();
			}
			/*
			 * iterate over testSet,
			 * whenever current elt == toRemove then remove
			 * increment listIt
			 */
			String current = setIt.next();
			if (current.equals(toRemove))
			{
				try
				{
					setIt.remove();
				}
				catch (IllegalStateException e)
				{
					fail(testName + " failed to remove elt " + toRemove);
				}

				for (String elt : testSet)
				{
					assertFalse(elt.equals(toRemove),
					            testName + " found element " + toRemove
					                + " after it.remove()");
				}
				assertEquals(--setSize,
				             testSet.size(),
				             testName + " unexpected set size after it.remove()");
				if (listIt.hasNext())
				{
					toRemove = listIt.next();
				}
			}
		}

		assertEquals(listIt.hasNext(),
		             setIt.hasNext(),
		             testName + " unexpected hasNext() value at the end of "
		             	+ "iteration (with remove in different order)");
	}

	/**
	 * Test method for {@link sets.Set#remove(java.lang.Object)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("remove(Object)")
	final void testRemoveObject(Class<? extends Set<String>> type)
	{
		String baseTestName = "remove(Object)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");
		/*
		 * Check we can't remove null objets
		 */
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testSet.remove(null);
		             },
		             testName + " remove(null) successfully called");

		testSet = constructSet(testName, type, listElements);

		List<String> foreignElements = new ArrayList<>(Arrays.asList(elements3));
		for (Iterator<String> it = foreignElements.iterator(); it.hasNext(); )
		{
			String toRemove = it.next();
			assertFalse(testSet.remove(toRemove),
			            testName + " successfully removed " + toRemove);
		}

		List<String> shuffledUniqueElements = new ArrayList<>(Arrays.asList(shuffleArray(unionElements)));
		for (Iterator<String> it = shuffledUniqueElements.iterator(); it.hasNext(); )
		{
			String toRemove = it.next();
			assertTrue(testSet.remove(toRemove),
			           testName + " failed to remove " + toRemove);
			for (String elt : testSet)
			{
				assertFalse(elt.equals(toRemove),
				            testName + " elt " + toRemove + " found after remove call");
			}
		}

		/*
		 * The implementation of ArraySet#remove and ArrayListSet#remove is
		 * supposed to be much faster than Set#remove, so we'll test it :
		 */
		long currentDuration = 0;
		long superDuration = 0;
		for (int i = 0; i < nbTests; i++)
		{
			testSet = constructSet(testName, type, listElements);
			long start;
			for (Iterator<String> it = shuffledUniqueElements.iterator(); it.hasNext();)
			{
				String eltToRemove = it.next();

				start = System.nanoTime();
				boolean cRemoved = testSet.remove(eltToRemove);
				currentDuration += System.nanoTime() - start;

				assertTrue(cRemoved,
				           testName + " unexpected remove(" + eltToRemove + ") result");
			}

			// re-fill set
			testSet = constructSet(baseTestName, type, listElements);
			assertNotNull(testSet,
			              testName + " non null empty instance failed");
			AbstractSet<String> abstractTestSet = (AbstractSet<String>) testSet;
			for (Iterator<String> it = shuffledUniqueElements.iterator(); it.hasNext();)
			{
				String eltToRemove = it.next();

				start = System.nanoTime();
				boolean sRemoved = abstractTestSet.superRemove(eltToRemove);
				superDuration += System.nanoTime() - start;

				assertTrue(sRemoved,
				           testName + " unexpected super remove(" + eltToRemove + ") result");
			}
		}

		double currentMeanDuration = currentDuration / (double)nbTests;
		double superMeanDuration = superDuration / (double)nbTests;

		System.out.println("\t" + testName + " = " + currentMeanDuration
			       		    + " ns, Set." + baseTestName + " = " + superMeanDuration
			       		    + " ns");

		assertTrue(currentMeanDuration < superMeanDuration,
		           testName + " implementation in class " + testSetTypeName
		               + " (" + currentMeanDuration
		               + " ns) is not faster than implementation in Set ("
		               + superMeanDuration + " ns)");
	}

	/**
	 * Test method for {@link sets.Set#removeAll(java.util.Collection)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("removeAll(Collection)")
	final void testRemoveAllCollectionOfQ(Class<? extends Set<String>> type)
	{
		String baseTestName = "removeAll(Collection)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");
		/*
		 * Check we can't removeAll from null Collections
		 */
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testSet.removeAll(null);
		             },
		             testName + " removeAll(null) successfully called");

		testSet = constructSet(testName, type, listElements);

		List<String> foreignElements = new ArrayList<>(Arrays.asList(elements3));
		assertFalse(testSet.removeAll(foreignElements),
		            testName + " successfully removed non present elements");

		List<String> shuffledElements = new ArrayList<>(Arrays.asList(shuffleArray(elements)));
		assertTrue(testSet.removeAll(shuffledElements),
		           testName + " not all elements have been removed");
		assertFalse(testSet.removeAll(shuffledElements),
		            testName + " same collection can't be removed twice!");

		for (Iterator<String> it = shuffledElements.iterator(); it.hasNext();)
		{
			String checkElement = it.next();
			for (String elt : testSet)
			{
				assertFalse(elt.equals(checkElement),
				            testName + " elt " + checkElement
				                + " found after removeAll call");
			}
		}

		testSet = constructSet(testName, type, listElements);

		/*
		 * Test removing a collection containing foreign (non String) element
		 * will throw a ClassCastException
		 */
		List<Object> objectList = new ArrayList<>(listElements);
		Integer surplus = 5;
		objectList.add(surplus);

		assertThrows(ClassCastException.class,
		             () -> {
		            	 testSet.removeAll(objectList);
		             },
		             testName + " removeAll with foreign element didn't throw");

		testSet = constructSet(testName, type, listElements);
		List<String> overSizedElements = new ArrayList<>(listElements);
		overSizedElements.addAll(Arrays.asList(elements3));
		// remove first element from overSizedElements
		String firstElement = overSizedElements.get(0);
		overSizedElements.remove(firstElement);
		/*
		 * After this call returns, testSet will contain no common elements
		 * with overSizedElements.
		 */
		assertTrue(testSet.removeAll(overSizedElements),
		           testName + " successfully removed all supplementary elements");
		// Check that there no elts from overSizedElements remaining
		for (String listElt : overSizedElements)
		{
			for (String elt : testSet)
			{
				assertFalse(elt.equals(listElt),
				            testName + " found " + listElt + " in remaining elements");
			}
		}
		// And Check that testSet still contains firstElement
		boolean firstFound = false;
		for (String elt : testSet)
		{
			if (elt.equals(firstElement))
			{
				firstFound = true;
				break;
			}
		}
		assertTrue(firstFound,
		           testName + " " + firstElement + " not found in remaining elements");
	}


	/**
	 * Test method for {@link sets.Set#retainAll(java.util.Collection)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 * - {@link Set#containsAll(Collection)}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("retainAll(Collection)")
	final void testRetainAllCollectionOfQ(Class<? extends Set<String>> type)
	{
		String baseTestName = "retainAll(Collection)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");
		/*
		 * Check we can't retainAll from null Collections on empty set
		 */
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testSet.retainAll(null);
		             },
		             testName + " retainAll(null) successfully called");

		testSet = constructSet(testName, type, listElements);

		/*
		 * Check we can't retainAll from null Collections on filled set
		 */
		assertThrows(NullPointerException.class,
		             () -> {
		            	 testSet.retainAll(null);
		             },
		             testName + " retainAll(null) successfully called");

		/*
		 * set should change when retaining non present elements
		 * expected resulting set should be empty
		 */
		List<String> foreignElements = new ArrayList<>(Arrays.asList(elements3));
		assertTrue(testSet.retainAll(foreignElements),
		           testName + " set has changed when retaining non present elements");
		int count = 0;
		for (Iterator<String> iterator = testSet.iterator(); iterator.hasNext();
			iterator.next())
		{
			count++;
		}
		assertEquals(0,
		             count,
		             testName + " resulting set does not have a 0 size");

		/*
		 * set shouldn't change when retaining already present elements
		 * expected resulting set should be unchanged
		 */
		testSet = constructSet(testName, type, listElements);
		List<String> shuffledElements = new ArrayList<>(Arrays.asList(shuffleArray(elements)));
		assertFalse(testSet.retainAll(shuffledElements),
		            testName + " set has changed when retaining already present elements");

		/*
		 * set should change when retaining a sub-set of already present elements
		 * expected resulting set should be reduced in size
		 */
		List<String> subElements1 = new ArrayList<>(Arrays.asList(elements1));
		assertTrue(testSet.retainAll(subElements1),
		           testName +  " resulting set hasn't changed when retaining part of its elements");
		assertTrue(testSet.containsAll(subElements1),
		            testName + " resulting set does not contains all elements of elements1");
		/*
		 * None of elements from elements2 should remain
		 */
		List<String> subElements2 = new ArrayList<>(Arrays.asList(elements2));
		assertFalse(testSet.containsAll(subElements2),
		            testName + " resulting set still contains elements from elements2");
	}

	/**
	 * Test method for {@link sets.Set#toArray()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("toArray()")
	final void testToArray(Class<? extends Set<String>> type)
	{
		String baseTestName = "toArray()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		Object[] array = testSet.toArray();
		assertNotNull(array,
		              testName + " resulting array is null");
		assertEquals(0,
		             array.length,
		             testName + " resulting array from empty set is not empty");

		testSet = constructSet(testName, type, listElements);
		array = testSet.toArray();
		assertEquals(unionElements.length,
		             array.length,
		             testName + " unexpected resulting array size from filled set");
		assertArrayEquals(unionElements,
		                  array,
		                  testName + " unexpected resulting array from filled set");
	}

	/**
	 * Test method for {@link sets.Set#toArray(T[])}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("toArray(T[])")
	final void testToArrayTArray(Class<? extends Set<String>> type)
	{
		String baseTestName = "toArray(T[])";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		/*
		 * Expect NullPointerException when specimen array is null
		 */
		assertThrows(NullPointerException.class,
		             () -> {
		            	 String[] spec = null;
		            	 @SuppressWarnings("unused")
		            	 String[] result = testSet.toArray(spec);
		             },
		             testName + " successfully called toArray(null)");

		/*
		 * Resulting array should:
		 * 	- be empty
		 * 	- be the same as specimenArray since no reallocation is necessary
		 */
		String[] specimenArray = new String[0];
		String[] resultArray = testSet.toArray(specimenArray);
		assertEquals(0,
		             resultArray.length,
		             testName + " resulting array from empty set is not empty");
		assertSame(specimenArray,
		           resultArray,
		           testName + " specimen and resulting array are not the same for empty set");

		/*
		 * Resulting array should:
		 * 	- have the same content as allSingleElements
		 * 	- be distinct from specimen array since reallocation was required
		 */
		testSet = constructSet(testName, type, listElements);
		resultArray = testSet.toArray(specimenArray);
		assertEquals(unionElements.length,
		             resultArray.length,
		             testName + " unexpected resulting array size from filled set");
		assertArrayEquals(unionElements,
		                  resultArray,
		                  testName + " unexpected resulting array from filled set");
		assertNotSame(specimenArray,
		              resultArray,
		              testName + " result array is the same as specimen array");
		/*
		 * Resulting array should:
		 * 	- have the same content as allSingleElements
		 * 	- be the same as specimenArray since no reallocation is necessary
		 */
		specimenArray = new String[testSet.size()];
		resultArray = testSet.toArray(specimenArray);
		assertEquals(unionElements.length,
		             resultArray.length,
		             testName + " unexpected resulting array size from filled set");
		assertArrayEquals(unionElements,
		                  resultArray,
		                  testName + " unexpected resulting array from filled set");
		assertSame(specimenArray,
		           resultArray,
		           testName + " result array is the same as specimen array");

	}

	/**
	 * Test method for {@link sets.Set#equals(java.lang.Object)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("equals(Object)")
	final void testEqualsObject(Class<? extends Set<String>> type)
	{
		String baseTestName = "equals(Object)";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");
		/*
		 * Set can't be equal to null
		 */
		assertFalse(testSet.equals(null),
		            testName + " testSet should not be equal to null");

		/*
		 * Set is equal to itself
		 */
		assertTrue(testSet.equals(testSet),
		           testName + " testSet should be equal to itself");

		/*
		 * Set is not equal to any other type of object
		 */
		assertFalse(testSet.equals(new Object()),
		            testName + " testSet should not be equal to other type object");

		testSet = constructSet(testName, type, listElements);
		/*
		 * Equals between different kinds of Sets
		 */
		List<String> shuffledElements = new ArrayList<>(Arrays.asList(elements));
		for (int i = 0; i < setTypes.length; i++)
		{
			Set<String> identicalSet = constructSet(testName, setTypes[i], listElements);
			Set<String> sameSet = constructSet(testName, setTypes[i], shuffledElements);
			Set<String> differentSet = constructSet(testName, setTypes[i], Arrays.asList(elements3));

			assertTrue(testSet.equals(identicalSet),
			           testName + " " + testSetType + ".equals("
			               + setTypes[i].getSimpleName() + " identicalSet) failed");
			assertTrue(testSet.equals(sameSet),
			           testName + " " + testSetType + ".equals("
			               + setTypes[i].getSimpleName() + " sameSet) failed");
			assertFalse(testSet.equals(differentSet),
			           testName + " " + testSetType + ".equals("
			               + setTypes[i].getSimpleName() + " differentSet) succeeded");
		}
	}

	/**
	 * Test method for {@link sets.Set#equals(java.lang.Object)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("clone()")
	final void testClone(Class<? extends Set<String>> type)
	{
		String baseTestName = "clone()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		Object cloned = testSet.clone();

		/*
		 * The cloned set should
		 * 	- not be null
		 * 	- have the same class as the clone source
		 * 	- be distinct from clone source
		 * 	- be equal to clone source
		 */
		assertNotNull(cloned,
		              testName + " clone creation failed");
		assertEquals(testSetType,
		             cloned.getClass(),
		             testName + " cloned set unexpected class "+ cloned.getClass().getSimpleName());
		assertNotSame(testSet,
		              cloned,
		              testName + " indistinct clone from source");
		assertEquals(testSet,
		             cloned,
		             testName + " clone has not same content");
		/*
		 * Same with filled set
		 */
		testSet  = constructSet(testName, type, listElements);
		assertNotNull(testSet,
		              testName + " filled set creation failed");
		cloned = testSet.clone();
		assertNotNull(cloned,
		              testName + " filled clone creation failed");
		assertEquals(testSetType,
		             cloned.getClass(),
		             testName + " filled cloned set unexpected class "+ cloned.getClass().getSimpleName());
		assertNotSame(testSet,
		              cloned,
		              testName + " indistinct filled clone from source");
		assertEquals(testSet,
		             cloned,
		             testName + " filled clone has not same content");
	}

	/**
	 * Test method for {@link sets.Set#toString()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("toString()")
	final void testToString(Class<? extends Set<String>> type)
	{
		String baseTestName = "toString()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		String expected = "{}";
		assertEquals(expected,
		             testSet.toString(),
		             testName + " unexpected toString output");

		testSet = constructSet(testName, type, listElements);
		StringBuilder expectedSb = new StringBuilder();
		expectedSb.append('{');
		for (int i = 0; i < unionElements.length; i++)
		{
			expectedSb.append(unionElements[i]);
			if (i < (unionElements.length - 1))
			{
				expectedSb.append(", ");
			}
		}
		expectedSb.append('}');
		assertEquals(expectedSb.toString(),
		             testSet.toString(),
		             testName + " unexpected toString output");
	}

	/**
	 * Test method for {@link sets.Set#union(sets.Set, sets.Set, sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("Set.union(Set, Set, Set)")
	final void testUnionSetOfESetOfESetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "Set.union(Set, Set, Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".union(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> result = constructSet(testName, currentType, null);
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set.union(testSet, other, result);

			assertEquals(unionElements.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, unionElements),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Unions where one of the set is null
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.union(null, other, result);
			             },
			             subTestName + " Set.union(null, other, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.union(testSet, null, result);
			             },
			             subTestName + " Set.union(testSet, null, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.union(testSet, other, null);
			             },
			             subTestName + " Set.union(testSet, other, null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#union(sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("union(Set)")
	final void testUnionSetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "union(Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".union(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set<String> result = testSet.union(other);

			assertEquals(testSetType,
			             result.getClass(),
			             subTestName + " unexpected result set type: "
			             + result.getClass().getSimpleName());
			assertEquals(unionElements.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, unionElements),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Union with null set
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 testSet.union(null);
			             },
			             subTestName + " testSet.union(null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#intersection(sets.Set, sets.Set, sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("Set.intersection(Set, Set, Set)")
	final void testIntersectionSetOfESetOfESetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "Set.intersection(Set, Set, Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".intersection(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> result = constructSet(testName, currentType, null);
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set.intersection(testSet, other, result);

			assertEquals(intersectionElements.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, intersectionElements),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Intersections where one of the set is null
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.intersection(null, other, result);
			             },
			             subTestName + " Set.intersection(null, other, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.intersection(testSet, null, result);
			             },
			             subTestName + " Set.intersection(testSet, null, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.intersection(testSet, other, null);
			             },
			             subTestName + " Set.intersection(testSet, other, null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#intersection(sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("intersection(Set)")
	final void testIntersectionSetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "intersection(Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".intersection(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set<String> result = testSet.intersection(other);

			assertEquals(testSetType,
			             result.getClass(),
			             subTestName + " unexpected result set type: "
			             + result.getClass().getSimpleName());
			assertEquals(intersectionElements.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, intersectionElements),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Intersection with null set
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 testSet.intersection(null);
			             },
			             subTestName + " testSet.intersection(null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#difference(sets.Set, sets.Set, sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("Set.difference(Set, Set, Set)")
	final void testDifferenceSetOfESetOfESetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "Set.difference(Set, Set, Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".difference(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> result = constructSet(testName, currentType, null);
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set.difference(testSet, other, result);

			assertEquals(complementElements1.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, complementElements1),
			           subTestName +  " comparison with expected elements failed");

			result = constructSet(testName, currentType, null);
			Set.difference(other, testSet, result);

			assertEquals(complementElements2.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, complementElements2),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Differences where one of the set is null
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set<String> result1 = constructSet(testName, currentType, null);
			            	 Set.difference(null, other, result1);
			             },
			             subTestName + " Set.difference(null, other, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set<String> result2 = constructSet(testName, currentType, null);
			            	 Set.difference(testSet, null, result2);
			             },
			             subTestName + " Set.difference(testSet, null, result) didn't throw");
			assertThrows(NullPointerException.class,
			             () -> {
			            	 Set.difference(testSet, other, null);
			             },
			             subTestName + " Set.difference(testSet, other, null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#difference(sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("difference(Set)")
	final void testDifferenceSetOfE(Class<? extends Set<String>> type)
	{
		String baseTestName = "difference(Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".difference(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set<String> result = testSet.difference(other);

			assertEquals(testSetType,
			             result.getClass(),
			             subTestName + " unexpected result type: "
			             + result.getClass().getSimpleName());
			assertEquals(complementElements1.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, complementElements1),
			           subTestName +  " comparison with expected elements failed");

			result = other.difference(testSet);

			assertEquals(currentType,
			             result.getClass(),
			             subTestName + " unexpected result type: "
			             + result.getClass().getSimpleName());
			assertEquals(complementElements2.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, complementElements2),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Difference with null set
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 testSet.difference(null);
			             },
			             subTestName + " Set.difference(null, other, result) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#symmetricDifference(sets.Set)}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("symmetricDifference(Set)")
	final void testSymmetricDifference(Class<? extends Set<String>> type)
	{
		String baseTestName = "symmetricDifference(Set)";
		List<String> listElements1 = new ArrayList<>(Arrays.asList(elements1));
		List<String> listElements2 = new ArrayList<>(Arrays.asList(elements2));
		setUpTest(constructSet(baseTestName, type, listElements1), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(elements1.length,
		             testSet.size(),
		             testName + " initial filled set doesn't have the right size");

		for (int i = 0; i < setTypes.length; i++)
		{
			Class<? extends Set<String>> currentType = setTypes[i];
			String currentTypeName = currentType.getSimpleName();
			String subTestName = testName + ".symmetricDifference(" + testSetTypeName + ", "
                + currentTypeName + ", " + currentTypeName
                + ")";
			Set<String> other = constructSet(testName, currentType, listElements2);

			Set<String> result = testSet.symmetricDifference(other);

			assertNotNull(result,
			              testName + " null result");
			assertEquals(testSetType,
			             result.getClass(),
			             subTestName + " unexpected result set type: "
			             + result.getClass().getSimpleName());
			assertEquals(diffElements.length,
			             result.size(),
			             subTestName + " failed with wrong result size");
			assertTrue(compareSet2Array(testName, result, diffElements),
			           subTestName +  " comparison with expected elements failed");
			/*
			 * Intersection with null set
			 */
			assertThrows(NullPointerException.class,
			             () -> {
			            	 testSet.symmetricDifference(null);
			             },
			             subTestName + " testSet.symmetricDifference(null) didn't throw");
		}
	}

	/**
	 * Test method for {@link sets.Set#elementsType()}.
	 * @param type the type of set to test provided by {@link #setClassesProvider()}
	 * @implNote Dependencies:
	 * - {@link Set#size()}
	 */
	@ParameterizedTest
	@MethodSource("setClassesProvider")
	@DisplayName("elementsType()")
	final void testElementsType(Class<? extends Set<String>> type)
	{
		String baseTestName = "elementsType()";
		setUpTest(constructSet(baseTestName, type, null), baseTestName);

		assertNotNull(testSet,
		              testName + " non null empty instance failed");
		assertEquals(0,
		             testSet.size(),
		             testName + " initial empty set doesn't have 0 size");

		assertNull(testSet.elementsType(),
		           testName + " elements type on empty set wasn't null");

		testSet = constructSet(testName, type, listElements);
		Class<?> eltType = testSet.elementsType();
		assertNotNull(eltType,
		              testName + " elements type on filled set was null");
		assertEquals(String.class,
		             eltType,
		             testName + " unexpected Set content type: " + eltType.getSimpleName());
	}
}
