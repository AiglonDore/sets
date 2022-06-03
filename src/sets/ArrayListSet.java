package sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

/**
 * A Concrete implementation of {@link Set} based on partial implementation from
 * {@link AbstractSet} using an internal {@link ArrayList}
 * @author davidroussel
 * @param <E> the type of elements in this set
 */
public class ArrayListSet<E> extends AbstractSet<E>
{
	/**
	 * The internal ArrayList to store elements
	 */
	private ArrayList<E> list;

	/**
	 * Default constructor.
	 * Creates an empty set
	 */
	public ArrayListSet()
	{
		// DONE 200 ArrayListSet#ArrayListSet(): replace with implementation
		list = new ArrayList<E>();
	}

	/**
	 * Copy constructor from collection.
	 * Creates a set able to contains the elements contained in c.
	 * All non-duplicates and non-null elements of collection c are copied to
	 * this set.
	 * @param c the collection to copy in this set
	 * @post All non duplicates elements of c have been added to this set
	 */
	public ArrayListSet(Collection<? extends E> c)
	{
		// DONE 201 ArrayListSet#ArrayListSet(Collection): replace with implementation
		list = new ArrayList<E>();
		for (E elt : c)
		{
			list.add(elt);
		}
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
	 */
	@Override
	public boolean add(E e) throws NullPointerException
	{
		// DONE 202 ArrayListSet#add(E): replace with implementation
		if (e == null) throw new NullPointerException();
		
		return list.add(e);
	}

	/**
	 * Clears all elements in this set.
	 * @implNote Potentially Faster implementation than {@link Set#clear()}
	 */
	@Override
	public void clear()
	{
		// DONE 203 ArrayListSet#clear(): replace with implementation
		list.clear();
	}

	/**
	 * Checks if this set contains object o.
	 * @param o the object to serach in this set
	 * @return true if object o was found in this set, false if object wasn't
	 * found in this set or if object o was null.
	 * @implNote The expected behavior of this method differs from the default
	 * expected behavior specified in {@link Collection#contains(Object)} as it
	 * doesn't throw {@link NullPointerException} when provided object is null.
	 * It just returns false.
	 * @implNote Potentially Faster implementation than {@link Set#contains(Object)}
	 */
	@Override
	public boolean contains(Object o)
	{
		// DONE 204 ArrayListSet#contains(Object): replace with implementation
		return list.contains(o);
	}

	/**
	 * Check if this set is empty.
	 * @return true if this set is empty
	 * @implNote Potentially Faster implementation than {@link Set#isEmpty()}
	 */
	@Override
	public boolean isEmpty()
	{
		// DONE 205 ArrayListSet#isEmpty(): replace with implementation
		return list.isEmpty();
	}

	/**
	 * Iterator factory method
	 * @return an iterator to the elements of this set
	 */
	@Override
	public Iterator<E> iterator()
	{
		// DONE 206 ArrayListSet#iterator(): replace with implementation
		// Hint: does #list have an iterator ?
		return list.iterator();
	}

	/**
	 * Removes an element o from the set only if this object is part of the set
	 * @param o the object to remove from set
	 * @return true if element o was part of this set and removed, false otherwise
	 * @throws NullPointerException if the object to remove is null
	 * @implNote Potentially Faster implementation than {@link Set#remove(Object)}
	 */
	@Override
	public boolean remove(Object o) throws NullPointerException
	{
		// DONE 207 ArrayListSet#remove(Object): replace with implementation
		return list.remove(o);
	}

	/**
	 * Number of elements in this set
	 * @return the current number of elements in this set
	 * @implNote Potentially Faster implementation than {@link Set#size()}
	 */
	@Override
	public int size()
	{
		// DONE 208 ArrayListSet#size(): replace with implementation
		return list.size();
	}

	/**
	 * Conversion to array of objects
	 * @return an new Object[] array containing all the elements of this set
	 */
	@Override
	public Object[] toArray()
	{
		// DONE 209 ArrayListSet#toArray(): replace with implementation
		return list.toArray();
	}

	/**
	 * Conversion to array of elements.
	 * Returns an array containing all of the elements in this Set in the
	 * same order; the runtime type of the returned array is that of the
	 * specified array. If the Set fits in the specified array, it is
	 * returned therein. Otherwise, a new array is allocated with the runtime
	 * type of the specified array and the size of this Set.
	 * If the Set fits in the specified array with room to spare (i.e., the
	 * array has more elements than the Set), the element in the array
	 * immediately following the end of the Set is set to null. (This is
	 * useful in determining the length of the Set only if the caller knows
	 * that the Set does not contain any null elements.)
	 * @return a new array of T containing all elements of this set
	 * @see Vector#toArray() implementation for inspiration
	 */
	@Override
	public <T> T[] toArray(T[] a)
	{
		// DONE 210 ArrayListSet#toArray(T[]): replace with implementation
		if (a.length >= this.size())
		{
			int  i = 0;
			for (E elt : list)
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
			a = Arrays.copyOf(a, list.size());
			return this.toArray(a);
		}
	}

	// -------------------------------------------------------------------------
	// Object overrides
	// -------------------------------------------------------------------------
	/**
	 * Returns a clone of this set. The copy will contain a reference to a
	 * clone of the internal llist, not a reference to the original
	 * internal list
	 * @return a new ArrayListSet<E> whith the same content
	 */
	@Override
	public Object clone()
	{
		// DONE 211 ArrayListSet#clone(): replace with implementation
		return new ArrayListSet((Collection<? extends E>)list.clone());
	}

	// -------------------------------------------------------------------------
	// Set<E> overrides
	// -------------------------------------------------------------------------

	/**
	 * Union of this set with another set: this ⋃ other
	 * @param other the set to create union with
	 * @return a new {@link ArrayListSet} containing the union of elements of
	 * this set and other set
	 * @see Set#union(Set, Set, Set)
	 */
	@Override
	public Set<E> union(Set<E> other)
	{
		// DONE 212 ArrayListSet#union(Set): replace with implementation
		Set<E> result = new ArrayListSet<E>(this);
		for (E elt : other)
		{
			result.add(elt);
		}
		return result;
	}

	/**
	 * Intersection of this set with another set: this ⋂ other
	 * @param other the set to create intersection with
	 * @return a new {@link ArrayListSet} containing the intersection of elements
	 * of this set, and the other set
	 * @see Set#intersection(Set, Set, Set)
	 */
	@Override
	public Set<E> intersection(Set<E> other)
	{
		// DONE 213 ArrayListSet#intersection(Set): replace with implementation
		Set<E> result = new ArrayListSet<E>();
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
	 * @return a new {@link ArrayListSet} containing the difference of elements
	 * of this set and the other set
	 * @see Set#difference(Set, Set, Set)
	 */
	@Override
	public Set<E> difference(Set<E> other)
	{
		// DONE 214 ArrayListSet#difference(Set): replace with implementation
		Set<E> result = new ArrayListSet<E>();
		for (E elt : this)
		{
			if (!other.contains(elt))
			{
				result.add(elt);
			}
		}
		return result;
	}
}
