package sets;

/**
 * Partial implementation of {@link Set}.
 * Contains all operations implementations that can be carried out using
 * other (potentially still abstract) operations.
 * @author davidroussel
 * @param <E> the type of elements in this set
 */
public abstract class AbstractSet<E> implements Set<E>
{
	// -------------------------------------------------------------------------
	// Object overrides
	// -------------------------------------------------------------------------
	/**
	 * Public implementation of clone method ({@link Object#clone()} is protected)
	 * @return a distinct copy of the current Set
	 * @implSpec This method needs to be re-declared in {@link AbstractSet}
	 * otherwise the {@link Object#clone} protected method is assumed
	 * which also throws {@link UnsupportedOperationException}
	 */
	@Override
	public abstract Object clone();

	/**
	 * Comparison with another object
	 * @return true if object o is also a Set which contains the same elements
	 * not necessarily in the same order.
	 */
	@Override
	public boolean equals(Object o)
	{
		// TODO 100 AbstractSet#equals(Object): replace with implementation ...
		return false;
	}


	/**
	 * Hashcode of this set: defined as the sum of all elements hashcodes
	 * @return the hashcode of this set
	 */
	@Override
	public int hashCode()
	{
		int hash = 0;
		// TODO 101 AbstractSet#hashCode(): replace with implementation ...
		return hash;
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

		// TODO 102 AbstractSet#toString(): replace with implementation ...

		builder.append('}');
		return builder.toString();
	}

	// -------------------------------------------------------------------------
	// Set<E> super calls
	// -------------------------------------------------------------------------
	/**
	 * Explicit call to {@link Set#clear()} from sub-classes for
	 * performance comparison purposes
	 */
	public void superClear()
	{
		Set.super.clear();
	}

	/**
	 * Explicit call to {@link Set#contains(Object)} from sub-classes
	 * for performance comparison purposes
	 * @param o the object to search in this set
	 * @return true if object o was found in this set, false if object wasn't
	 * found in this set or if object o was null.
	 */
	public boolean superContains(Object o)
	{
		return Set.super.contains(o);
	}

	/**
	 * Explicit call to {@link Set#isEmpty()} from sub-classes for
	 * performance comparison purposes
	 * @return true if this set is empty
	 */
	public boolean superIsEmpty()
	{
		return Set.super.isEmpty();
	}

	/**
	 * Explicit call to {@link Set#remove(Object)} from sub-classes for
	 * performance comparison purposes
	 * @param o the object to remove from set
	 * @return true if element o was part of this set and removed, false
	 * otherwise
	 * @throws NullPointerException if the object to remove is null
	 */
	public boolean superRemove(Object o) throws NullPointerException
	{
		return Set.super.remove(o);
	}

	/**
	 * Explicit call to {@link Set#size()} from sub-classes for
	 * performance comparison purposes
	 * @return the current number of elements in this set
	 */
	public int superSize()
	{
		return Set.super.size();
	}
}
