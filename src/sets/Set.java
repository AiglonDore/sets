package sets;

import java.util.Collection;
import java.util.Iterator;

/**
 * Interface defining what a set is:
 * - A set is a collection of elements without duplicates and null elements in which
 * the order of the elements does not matter (for comparing purposes): e.g. two
 * sets containing the same elements in different order are considered to be equals
 * - Furthermore a set defines several set operations such as
 * 	- union
 * 	- intersection
 * 	- difference
 * 	- symetric difference
 * @author David Roussel and Aiglon Doré
 * @param <E> the type of elements in this set
 */
public interface Set<E> extends Collection<E>
{
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
	public abstract boolean add(E e) throws NullPointerException;

	/**
	 * Adds to this set all elements in c, regardless of the fact that
	 * collection c might contain multiple instances of elements. Only one
	 * instance of similar elements should be added to this set as it should
	 * not contain duplicates.
	 * @return true if this set has been modified by adding (at least) one
	 * element from c, false otherwise.
	 * @throws NullPointerException if the specified collection is null
	 * @throws NullPointerException if one element in specified collection is
	 * null, since this set doesn't allow null elements.
	 */
	@Override
	public default boolean addAll(Collection<? extends E> c) throws NullPointerException
	{
		// DONE 000 Set#addAll(Collection): replace with implementation using #add(Object)
		boolean output = false;
		
		if (c == null) throw new NullPointerException();
		
		for (Iterator<? extends E> it =c.iterator();it.hasNext();)
		{
			E elt = (E) it.next();
			if (elt == null)
			{
				throw new NullPointerException();
			}
			
			if (!this.contains(elt))
			{
				output = this.add(elt);
			}
		}
		
		return output;
	}

	/**
	 * Clears all elements in this set.
	 */
	@Override
	public default void clear()
	{
		// DONE 001 Set#clear(): replace with implementation using the iterator
		for (Iterator<E> it = this.iterator();it.hasNext();)
		{
			it.next();
			it.remove();
		}
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
	 */
	@Override
	public default boolean contains(Object o)
	{
		// DONE 002 Set#contains(Object): replace with implementation using the iterator
		for (Iterator<E> it = this.iterator();it.hasNext();)
		{
			if (it.next().equals(o))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Check that all elements in Collection<?> c are present in this set,
	 * regardless of the fact that collection c might contain multiple instances
	 * of the same elements.
	 * @param c the collection to check
	 * @return true if all elements in c have been found in this set,
	 * false otherwise
	 * @throws NullPointerException if the provided collection is null
	 * @throws ClassCastException if the types of one or more elements in the
	 * specified collection are incompatible with this collection
	 * @note also corresponds to the "includes" relationship between sets
	 */
	@Override
	public default boolean containsAll(Collection<?> c) throws
		NullPointerException, ClassCastException
	{
		// DONE 003 Set#containsAll(Collection): replace with implementation using #contains(Object)
		if (c == null)
		{
			throw new NullPointerException();
		}
		for (Iterator<?> it = c.iterator();it.hasNext();)
		{
			Object elt = it.next();
			if (elementsType().isInstance(elt))
			{
				if (!contains(elt)) return false;
			}
			else
			{
				throw new ClassCastException();
			}
		}
		return true;
	}

	/**
	 * Check if this set is empty.
	 * @return true if this set is empty
	 */
	@Override
	public default boolean isEmpty()
	{
		// DONE 004 Set#isEmpty(): replace with implementation using the iterator
		return !(iterator().hasNext());
	}

	/**
	 * Iterator factory method
	 * @return an iterator to the elements of this set
	 */
	@Override
	public abstract Iterator<E> iterator();

	/**
	 * Removes an element o from the set only if this object is part of the set
	 * @param o the object to remove from set
	 * @return true if element o was part of this set and removed, false otherwise
	 * @throws NullPointerException if the object to remove is null
	 */
	@Override
	public default boolean remove(Object o) throws NullPointerException
	{
		// DONE 005 Set#remove(Object): replace with implementation using the iterator
		if (o == null) throw new NullPointerException();
		for (Iterator<E> it = iterator(); it.hasNext();)
		{
			if (it.next().equals(o))
			{
				it.remove();
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes all elements of collection c from this set.
	 * After this call returns, this collection will contain no elements in
	 * common with the specified collection.
	 * @param c the collection of elements to remove from this set
	 * @return true if all distinct elements of collection c found in this set
	 * have been removed, false otherwise.
	 * @throws NullPointerException if the specified collection is null
	 * @throws ClassCastException if the type of one or more elements in this
	 * set are incompatible with the specified collection
	 * @post No elements from c can be found in this set
	 */
	@SuppressWarnings("unchecked")
	@Override
	public default boolean removeAll(Collection<?> c) throws
		NullPointerException, ClassCastException
	{
		// DONE 006 Set#removeAll(Collection) replace with implementation using #remove(Object)
		if (c == null)
		{
			throw new NullPointerException();
		}
		if (this.isEmpty() || c.isEmpty()) return false;
		Class<? extends E> elementsClass = elementsType();
		boolean output = false;
		for (Iterator<?> it = c.iterator(); it.hasNext();)
		{
			Object elt = it.next();
			if (elt != null)
			{
				if (elementsClass.isInstance(elt))
				{
					E obj = (E) elt;
					if (this.contains(obj))
					{
						this.remove(obj);
						output = true;
					}
				}
				else
				{
					throw new ClassCastException();
				}
			}
		}
		return output;
	}

	/**
	 * Retain in this set only the elements contained in collection c.
	 * @param c the collection containing the elements to retain in this set
	 * @return true if at least one element from this set has been removed.
	 * @throws NullPointerException if the specified collection is null
	 */
	@Override
	public default boolean retainAll(Collection<?> c) throws NullPointerException
	{
		// DONE 007 Set#retainAll(Collection): replace with implementation...
		if (c == null)
		{
			throw new NullPointerException();
		}
		boolean output = false;
		for (Iterator<E> it = iterator(); it.hasNext();)
		{
			E elt = it.next();
			if (!c.contains(elt))
			{
				it.remove();
				output = true;
			}
		}
		return output;
	}

	/**
	 * Number of elements in this set
	 * @return the current number of elements in this set
	 */
	@Override
	public default int size()
	{
		// DONE 008 Set#size(): replace with implementation using the iterator
		int length = 0;
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			it.next();
			length++;
		}
		return length;
	}

	/**
	 * Conversion to array of objects
	 * @return a new array of objects containing all elements of this set
	 */
	@Override
	public abstract Object[] toArray();

	/**
	 * Conversion to array of elements of type T
	 * @return a new array of T containing all elements of this set
	 * @throws NullPointerException if the specified array is null
	 */
	@Override
	public abstract <T> T[] toArray(T[] a) throws NullPointerException;

	// -------------------------------------------------------------------------
	// Object overrides
	// -------------------------------------------------------------------------
	/**
	 * Public declaration of clone method ({@link Object#clone()} is protected)
	 * @return a distinct copy of the current Set.
	 * @note interfaces can't provide a default implementation of methods defined
	 * in Object super-class (since they already have a base implementation).
	 */
	public abstract Object clone();

	/**
	 * Comparison with another object
	 * @return true if object o is also a Set which contains the same elements
	 * not necessarily in the same order.
	 * @note interfaces can't provide a default implementation of methods defined
	 * in Object super-class (since they already have a base implementation).
	 */
	@Override
	public abstract boolean equals(Object o);

	/**
	 * Hashcode of this set: defined as the sum of all elements hashcodes
	 * @return the hashcode of this set
	 * @note interfaces can't provide a default implementation of methods defined
	 * in Object super-class (since they already have a base implementation).
	 */
	@Override
	public abstract int hashCode();

	/**
	 * String representation of this set.
	 * @return a new String representing the elements of this set with the
	 * following format: {elt1, elt2, ..., eltn}
	 * @note interfaces can't provide a default implementation of methods defined
	 * in Object super-class (since they already have a base implementation).
	 */
	@Override
	public abstract String toString();

	// -------------------------------------------------------------------------
	// Set<E> specific methods
	// -------------------------------------------------------------------------

	/**
	 * Union of two sets into a result set: result = first ⋃ second
	 * @param <E> the type of elements in all sets
	 * @param first the first set
	 * @param second the second set
	 * @param result the resulting set containing all elements of set1 and set2
	 * @throws NullPointerException if any of the provided sets are null
	 * @see #union(Set)
	 */
	public static <E> void union(Set<E> first, Set<E> second, Set<E> result) throws NullPointerException
	{
		// DONE 009 Set#union(Set, Set, Set): replace with implementation...
		if (first == null || second == null || result == null) throw new NullPointerException();
		result.clear();
		result.addAll(first.union(second));
	}

	/**
	 * Union of this set with another set: this ⋃ other
	 * @param other the set to create union with
	 * @return a new set containing the union of elements of this set and set "set"
	 * @throws NullPointerException if the provided set is null
	 * @see #union(Set, Set, Set)
	 */
	public abstract Set<E> union(Set<E> other) throws NullPointerException;

	/**
	 * Intersection of two sets into a result set: result = first ⋂ second
	 * @param <E> the type of elements in all sets
	 * @param first the first set
	 * @param second the second set
	 * @param result the resulting set containing all common elements of set1 and set2
	 * @throws NullPointerException if any of the provided sets are null
	 * @see #intersection(Set)
	 */
	public static <E> void intersection(Set<E> first, Set<E> second, Set<E> result) throws NullPointerException
	{
		// DONE 010 Set#intersection(Set, Set, Set): replace with implementation...
		if (first == null || second == null || result == null) throw new NullPointerException();
		result.clear();
		result.addAll(first.intersection(second));
	}

	/**
	 * Intersection of this set with another set: this ⋂ other
	 * @param other the set to create intersection with
	 * @return a new set containing the intersection of elements of this set
	 * and the other set
	 * @throws NullPointerException if the provided set is null
	 * @see #intersection(Set, Set, Set)
	 */
	public abstract Set<E> intersection(Set<E> other) throws NullPointerException;

	/**
	 * Difference of two sets into a result set: result = first - second
	 * @param <E> the type of elements in all sets
	 * @param first the first set
	 * @param second the second set
	 * @param result the resulting set containing all elements of first set which
	 * are not part of second set.
	 * @throws NullPointerException if any of the provided sets are null
	 * @see #difference(Set)
	 */
	public static <E> void difference(Set<E> first, Set<E> second, Set<E> result) throws NullPointerException
	{
		// DONE 011 Set#difference(Set, Set, Set): replace with implementation...
		if (first == null || second == null || result == null) throw new NullPointerException();
		result.clear();
		result.addAll(first.difference(second));
	}

	/**
	 * Difference of this set with another set: this - other
	 * @param other the set to create difference with
	 * @return a new set containing the difference of elements of this set
	 * and the other set.
	 * @throws NullPointerException if the provided set is null
	 * @see #difference(Set, Set, Set)
	 */
	public abstract Set<E> difference(Set<E> other) throws NullPointerException;

	/**
	 * Symmetric difference of this set with another set: this Δ other
	 * @param other the other set to compute symmetric difference with
	 * @return a new set containing all elements which are part of this
	 * set but not part of the other set + the elements of the other set which
	 * are not part of this set (kind of a anti-intersection)
	 * @throws NullPointerException if the provided set is null
	 * @implSpec symmetric difference can be implemented as
	 * (this - other) ⋃ (other - this) or
	 * (this ⋃ other) - (this ⋂ other)
	 */
	public default Set<E> symmetricDifference(Set<E> other) throws NullPointerException
	{
		// DONE 012 Set#symmetricDifference(Set): replace with implementation...
		if (other == null) throw new NullPointerException();
		return this.difference(other).union(other.difference(this));
	}

	/**
	 * Provides the class of elements in this set (if possible)
	 * @return The {@link Class} signature of elements in this set
	 * if not empty or null if this set is empty
	 * @implNote Useful in methods requiring to throw {@link ClassCastException}
	 * such as {@link #containsAll(Collection)} and {@link #removeAll(Collection)}
	 */
	@SuppressWarnings("unchecked")
	public default Class<? extends E> elementsType()
	{
		// DONE 013 Set#elementsType(): replace with implementation...
		if (isEmpty()) return null;
		E elt = iterator().next();
		return (Class<? extends E>) elt.getClass();
	}
}
