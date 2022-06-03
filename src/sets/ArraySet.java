package sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

/**
 * A Concrete implementation of {@link Set} based on partial implementation from
 * {@link AbstractSet} using an internal array to store elements.
 * @author David Roussel and Aiglon Doré
 * @param <E> the type of elements in this set
 */
public class ArraySet<E> extends AbstractSet<E>
{
	/**
	 * The array buffer into which the components of this set are stored. The
	 * capacity of the set is the length of this array buffer, and is at
	 * least large enough to contain all the set's elements.
	 * Any array elements following the last element in the set are null.
	 * {@code
	 * 	elementData = (E[]) new Object[size];
	 * }
	 */
	private E[] elementData;

	/**
	 * The number of valid components in this set object. Components
	 * elementData[0] through elementData[elementCount-1] are the actual items.
	 */
	private int elementCount;

	/**
	 * The amount by which the capacity of the array is automatically
	 * incremented when its size becomes greater than its capacity.
	 * @implSpec this capacity increment shall never be null or negative.
	 */
	private int capacityIncrement;

	/**
	 * The default capacity if capacity is not provided during construction
	 * @implNote This is an internal constant which doesn't need to be public
	 * but we'll use it in the tests
	 */
	public static final int DefaultCapacity = 10;

	/**
	 * The default capacity increment if capacity increment is not provided
	 * during construction
	 */
	private static final int DefaultCapacityIncrement = 10;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Valued constructor
	 * @param initialCapacity the initial capacity of this set
	 * @param capacityIncrement the capacity increment of this set
	 * @throws IllegalArgumentException if either provided initialCapacity or
	 * capacityIncrement are less than 1.
	 */
	@SuppressWarnings("unchecked") // Required to cast Object[] into E[]
	public ArraySet(int initialCapacity, int capacityIncrement) throws IllegalArgumentException
	{
		if (initialCapacity < 1 || capacityIncrement < 1) throw new IllegalArgumentException();
		// DONE 300 ArraySet#ArraySet(int, int): replace with implementation
		elementData = (E[]) new Object[initialCapacity];
		elementCount = 0;
		this.capacityIncrement = capacityIncrement;
	}

	/**
	 * Default constructor.
	 * Creates an empty set of capacity {@link #DefaultCapacity} and
	 * capacity increment {@link #DefaultCapacityIncrement}
	 */
	@SuppressWarnings("unchecked")
	public ArraySet()
	{
		// DONE 301 ArraySet#ArraySet(): replace with implementation
		elementData = (E[]) new Object[DefaultCapacity];
		elementCount = 0;
		this.capacityIncrement = DefaultCapacityIncrement;
	}

	/**
	 * Partially valued constructor.
	 * Creates an empty set with initial capacity and {@link #DefaultCapacityIncrement}
	 * @param initialCapacity the initial capacity of this set
	 * @throws IllegalArgumentException if provided initialCapacity is less than 1.
	 */
	@SuppressWarnings("unchecked")
	public ArraySet(int initialCapacity) throws IllegalArgumentException
	{
		// DONE 302 ArraySet#ArraySet(int): replace with implementation
		if (initialCapacity < 1) throw new IllegalArgumentException();
		elementData = (E[]) new Object[initialCapacity];
		elementCount = 0;
		this.capacityIncrement = DefaultCapacityIncrement;
	}

	/**
	 * Copy constructor from collection.
	 * Creates a set able to contains (at least) the number of elements contained
	 * in c with {@link #DefaultCapacityIncrement}.
	 * All non-duplicates and non-null elements of collection c are copied to
	 * this set.
	 * @param c the collection to copy in this set
	 * @implSpec If collection c is empty then the expected capacity of this set
	 * should revert to {@link #DefaultCapacity}
	 */
	@SuppressWarnings("unchecked")
	public ArraySet(Collection<? extends E> c)
	{
		// DONE 303 ArraySet#ArraySet(Collection): replace with implementation
		if (c.isEmpty())
		{
			elementData = (E[]) new Object[DefaultCapacity];
			elementCount = 0;
		}
		else
		{
			elementCount = 0;
			elementData = (E[]) new Object[c.size()];
			for (E elt : c)
			{
				if (elt != null && !this.contains(elt))
				{
					elementData[elementCount] = elt;
					elementCount++;
				}
			}
		}
		capacityIncrement = ArraySet.DefaultCapacityIncrement;
	}

	// -------------------------------------------------------------------------
	// Collection<E> overrides
	// -------------------------------------------------------------------------

	/**
	 * Adds a new element to the set if and only if the element is not already
	 * part of this set and the element is not null.
	 * @param e the element to add
	 * @return true if the element has been added, false otherwise
	 * @throws NullPointerException if the object to add is null
	 * @implNote if the array buffer is full it needs to {@link #grow()}
	 */
	@Override
	public boolean add(E e) throws NullPointerException
	{
		// DONE 311 ArraySet#add(E): replace with implementation
		if (e == null) throw new NullPointerException();
		if (!contains(e))
		{
			if (elementCount == elementData.length)
			{
				grow();
			}
			elementData[elementCount] = e;
			elementCount++;
			return true;
		}
		return false;
	}

	/**
	 * Clears all elements in this set
	 * @implNote Faster implementation than {@link Set#clear()} since it
	 * doesn't need to use the iterator
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void clear()
	{
		// DONE 312 ArraySet#clear(): replace with implementation
		elementData = (E[]) new Object[DefaultCapacity];
	}

	/**
	 * Checks if this set contains object o.
	 * @param o the object to serach in this set
	 * @return true if object o was found in this set, false if object o wasn't
	 * found in this set or if object o was null.
	 * @implNote The expected behavior of this method differs from the default
	 * expected behavior specified in {@link Collection#contains(Object)} as it
	 * doesn't throw {@link NullPointerException} when provided object is null.
	 * It just returns false.
	 * @implNote Faster implementation than {@link Set#contains(Object)}
	 * since it doesn't need to use the iterator
	 */
	@Override
	public boolean contains(Object o)
	{
		// DONE 313 ArraySet#contains(Object): replace with implementation
		if (o == null) return false;
		for (int i = 0; i < elementCount; i++)
		{
			if (elementData[i].equals(o)) return true;
		}
		return false;
	}

	/**
	 * Check if this set is empty.
	 * @return true if this set is empty
	 * @implNote Faster implementation than {@link Set#isEmpty()} since
	 * it doesn't need to use the iterator
	 */
	@Override
	public boolean isEmpty()
	{
		// DONE 314 ArraySet#isEmpty(): replace with implementation
		return elementCount == 0;
	}

	/**
	 * Creates an new {@link ArraySetIterator} to iterate over
	 * {@link #elementData}.
	 */
	@Override
	public Iterator<E> iterator()
	{
		return new ArraySetIterator<E>();
	}

	/**
	 * Removes an element o from the set only if this object is part of the set
	 * @param o the object to remove from set
	 * @return true if element o was part of this set and removed, false
	 * otherwise
	 * @throws NullPointerException if the object to remove is null
	 * @implNote Faster implementation than {@link Set#remove(Object)}
	 * since it doesn't need to use iterator.
	 * @see #removeAtIndex(int)
	 */
	@Override
	public boolean remove(Object o) throws NullPointerException
	{
		// DONE 315 ArraySet#remove(Object): replace with implementation
		if (o == null) throw new NullPointerException();
		int index = -1;
		
		for (int i = 0; i < elementCount; i++)
		{
			if (elementData[i].equals(o))
			{
				index = i;
				break;
			}
		}
		
		return removeAtIndex(index);
	}

	/**
	 * Number of elements in this set
	 * @return the current number of elements in this set
	 * @implNote Faster implementation than {@link Set#size()} since it
	 * doesn't need to use iterator
	 */
	@Override
	public int size()
	{
		// DONE 316 ArraySet#size(): replace with implementation
		return elementCount;
	}

	/**
	 * Conversion to array of objects
	 * @return an new Object[] array containing all the elements of this set
	 */
	@Override
	public Object[] toArray()
	{
		// DONE 317 ArraySet#toArray(): replace with implementation
		Object[] output = elementData.clone();
		return output;
	}

	/**
	 * Conversion to array of elements.
	 * Returns an array containing all of the elements in this Set in the
	 * same order; the runtime type of the returned array is that of the
	 * specified array. If the Set fits in the specified array, it is
	 * returned therein (using
	 * {@link System#arraycopy(Object, int, Object, int, int)}). Otherwise, a
	 * new array is allocated with the runtime type of the specified array and
	 * the size of this Set (using {@link Arrays#copyOf(Object[], int, Class)}).
	 * If the Set fits in the specified array with room to spare (i.e., the
	 * array has more elements than the Set), the element in the array
	 * immediately following the end of the Set is set to null. (This is
	 * useful in determining the length of the Set only if the caller knows
	 * that the Set does not contain any null elements.)
	 * @return a new array of T containing all elements of this set
	 * @throws NullPointerException if the specified array is null
	 * @see Vector#toArray() implementation for inspiration
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		// DONE 318 ArraySet#toArray(T[]): replace with implementation
		if (a.length >= this.size())
		{
			int  i = 0;
			for (E elt : elementData)
			{
				a[i] = (T) elt;
				i++;
			}
			while (i < a.length)
			{
				a[i] = null;
				i++;
			}
			return a;
		}
		else
		{
			a = Arrays.copyOf(a, elementData.length);
			return this.toArray(a);
		}
	}

	// -------------------------------------------------------------------------
	// Object overrides
	// -------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	/**
	 * Returns a clone of this set. The copy will contain a reference to a
	 * clone of the internal data array, not a reference to the original
	 * internal data array of this ArraySet object.
	 * @return a new ArraySet<E> whith the same {@link #capacity()},
	 * {@link #capacityIncrement} and content.
	 */
	@Override
	public Object clone()
	{
		// DONE 319 ArraySet#clone(): replace with implementation
		ArraySet<E> newSet = new ArraySet<E>(this.capacity(),this.capacityIncrement);
		for (int i =0; i < elementCount; i++)
		{
			newSet.elementData[i] = this.elementData[i];
		}
		return newSet;
	}

	/**
	 * String representation of this set.
	 * @return a new String representing the elements of this set with the
	 * following format: {elt1, elt2, ..., eltn}
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append('{');

		// DONE 320 ArraySet#toString(): replace with implementation
		
		for (int i = 0; i < elementCount - 1; i++)
		{
			builder.append(elementData[i].toString());
			builder.append(", ");
		}
		if (elementCount > 1) builder.append(elementData[elementCount - 1].toString());

		builder.append('}');
		return builder.toString();
	}

	// -------------------------------------------------------------------------
	// Set<E> overrides
	// -------------------------------------------------------------------------

	/**
	 * Union of this set with another set: this ⋃ other
	 * @param other the set to create union with
	 * @return a new {@link ArraySet} containing the union of elements of this
	 * set and other set
	 * @throws NullPointerException if the provided set is null
	 * @see Set#union(Set, Set, Set)
	 */
	@Override
	public Set<E> union(Set<E> other) throws NullPointerException
	{
		// DONE 321 ArraySet#union(Set): replace with implementation
		// The new set has (at most) the size of both sets
		if (other == null) throw new NullPointerException();
		Set<E> result = new ArraySet<E>(other.size()+ this.size());
		result.addAll(this);
		result.addAll(other);
		return result;
	}

	/**
	 * Intersection of this set with another set: this ⋂ other
	 * @param other the set to create intersection with
	 * @return a new {@link ArraySet} containing the intersection of elements of
	 * this set and the other set
	 * @throws NullPointerException if the provided set is null
	 * @see Set#intersection(Set, Set, Set)
	 */
	@Override
	public Set<E> intersection(Set<E> other) throws NullPointerException
	{
		// DONE 322 ArraySet#intersection(Set): replace with implementation
		// The new set has (at most) the size of this
		if (other == null) throw new NullPointerException();
		Set<E> result = new ArraySet<E>(this.size());
		for (E elt : this)
		{
			if (other.contains(elt))
			{
				result.add(elt);
			}
		}
		return result;
	}

	/**
	 * Difference of this set with another set: this - other
	 * @param other the set to create difference with
	 * @return a new {@link ArraySet} containing the difference of elements of
	 * this set and the other set
	 * @throws NullPointerException if the provided set is null
	 * @see Set#difference(Set, Set, Set)
	 */
	@Override
	public Set<E> difference(Set<E> other) throws NullPointerException
	{
		// DONE 323 ArraySet#difference(Set): replace with implementation
		// The new set has (at most) the size of the smallest set
		if (other == null) throw new NullPointerException();
		Set<E> result = new ArraySet<E>(Math.min(this.size(),other.size()));
		return result;
	}

	// -------------------------------------------------------------------------
	// ArraySet<E> specific methods
	// -------------------------------------------------------------------------

	/**
	 * Current capacity
	 * @return the current capacity of this set (the number of elements the
	 * internal buffer ({@link #elementData}) can support without growing)
	 */
	public int capacity()
	{
		// DONE 304 ArraySet#capacity(): replace with implementation
		return elementData.length;
	}

	/**
	 * Strip the internal array to its minimal size to hold all the elements in
	 * this set using {@link Arrays#copyOf(Object[], int)} to reallocate
	 * {@link #elementData}, if and only if this set is not empty, otherwise
	 * {@link IllegalStateException} is thrown
	 * @throws IllegalStateException if the set is empty and can't be stripped.
	 * @post {@link #capacity()} and {@link #size()} should be equal
	 */
	public void strip() throws IllegalStateException
	{
		// DONE 305 ArraySet#strip(): replace with implementation
		if (isEmpty())
		{
			throw new IllegalStateException();
		}
		elementData = Arrays.copyOf(elementData, elementCount);
	}

	/**
	 * Remove element at index index.
	 * @param removeIndex the index of the element to remove
	 * @return true if the index was valid and the element at index has been
	 * removed, false otherwise.
	 * @implNote This method can be used in either {@link #remove(Object)} or
	 * {@link ArraySetIterator#remove()}
	 * @see #remove(Object)
	 * @see ArraySetIterator#remove()
	 */
	protected boolean removeAtIndex(int removeIndex)
	{
		// DONE 306 ArraySet#removeAtIndex(int): replace with implementation
		if ((removeIndex < 0) || (removeIndex >= elementCount))
		{
			return false;
		}

		/*
		 * If index is valid then move all subsequent objects one step left.
		 * Note: index remains unchanged
		 */
		for (int i = removeIndex; i < (elementCount - 1); i++)
		{
			elementData[i] = elementData[i+1];
		}
		elementData[--elementCount] = null;
		return true;
	}

	/**
     * Increases the capacity to ensure that it can hold at least the
     * number of elements specified by the minimum capacity argument.
     * @param minCapacity the desired minimum capacity
	 * @return a new array E[] able to contain minCapacity elements
	 * (iff minCapacity > current capacity, or current capacity +
	 * {@link #capacityIncrement} if minCapacity <= current capacity)
	 * @post {@link #elementData} has been modified
     */
	protected E[] grow(int minCapacity)
	{
		int oldCapacity = elementData.length;
		int newCapacity = (minCapacity < oldCapacity ? oldCapacity
		    + capacityIncrement : minCapacity);
		return elementData = Arrays.copyOf(elementData, newCapacity);
	}

    /**
     * Increases the capacity by {@link #capacityIncrement}
     * @return a new array E[] able to contain {@link #elementCount} +
     * {@link #capacityIncrement} elements
	 * @post {@link #elementData} has been modified
     */
    protected E[] grow()
    {
        return grow(elementCount + capacityIncrement);
    }

    // -------------------------------------------------------------------------
	// Internal iterator class
	// -------------------------------------------------------------------------

	/**
	 * Iterator for the {@link ArraySet}.
	 * This iterator only consists in an index indicating the current
	 * element to provide with {@link #next()} method and a "nextCalled" flag
	 * indicating next has been called and it is now legal to call remove if
	 * needed.
	 * @param <F> The type of element to iterate over
	 * @implNote We can't use <E> here since it would be masked by the <E> of
	 * {@link ArraySet}, so we're using a another one such as <F>.
	 * @author davidroussel
	 */
	protected class ArraySetIterator<F> implements Iterator<F>
	{
		/**
		 * The current index within {@link ArraySet#elementData}
		 */
		private int index;

		/**
		 * Flag indicating {@link #next()} has been called and it is now legal
		 * to call the {@link #remove()} method.
		 */
		private boolean nextCalled;

		/**
		 * Constructor
		 * Creates a new iterator by initializing the {@link #index} to 0 and
		 * {@link #nextCalled} to false
		 */
		public ArraySetIterator()
		{
			// DONE 307 ArraySetIterator#ArraySetIterator(): replace with implementation
			index = 0;
			nextCalled = false;
		}

		/**
		 * Returns true if the iteration has more elements.
		 * @return true if the iteration has more elements ({@link #index} is
		 * still lower than {@link ArraySet#elementCount})
		 */
		@Override
		public boolean hasNext()
		{
			// DONE 308 ArraySetIterator#hasNext(): replace with implementation
			return index < elementCount;
		}

		/**
		 * Returns the next element in the iteration.
		 * @return the next element in the iteration
		 * @throws NoSuchElementException if the iteration has no more elements
		 * @post The current element has been returned, {@link #index} has been
		 * incremented and {@link #nextCalled} has been set to true.
		 */
		@SuppressWarnings("unchecked") // because there might be a (F) cast
		@Override
		public F next() throws NoSuchElementException
		{
			// DONE 309 ArraySetIterator#next(): replace with implementation
			index++;
			if (index >= elementCount) throw new NoSuchElementException("Index is out of bounds.");
			nextCalled = true;
			return (F) elementData[index];
		}

		/**
		 * Removes from the underlying collection the last element returned by
		 * this iterator. This method can be called only once per call to
		 * {@link #next()}. Element removal can be performed with
		 * {@link ArraySet#removeAtIndex(int)}
		 * The behavior of an iterator is unspecified if the underlying
		 * collection is modified while the iteration is in progress in any way
		 * other than by calling this method, unless an overriding class has
		 * specified a concurrent modification policy.
		 * @throws IllegalStateException if the next method has not yet been
		 * called, or the remove method has already been called after the last
		 * call to the next method.
		 * @post {@link #nextCalled} has been reset to false after this call.
		 * @see ArraySet#removeAtIndex(int)
		 */
		@Override
		public void remove()
		{
			// DONE 310 ArraySetIterator#remove(): replace with implementation
			if (nextCalled)
			{
				removeAtIndex(index);
				nextCalled = false;
			}
			else throw new IllegalStateException("Next has not been called yet");
		}
	}
}
