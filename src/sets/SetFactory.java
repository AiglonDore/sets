package sets;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Factory to build a new instance of a {@link Set} that can be used in tests
 * @param <E> The type of elements contained in the set to create.
 * @author davidroussel
 */
public class SetFactory<E>
{
	/**
	 * Factory to create a new set based on required type and an optional
	 * content
	 * @param <E> The type of content for the created set
	 * @param setType type of set to create: either
	 * {@link sets.ArraySet} or
	 * ...
	 * @param content the optional content to add to the created set.
	 * @return a new set of the required type and content
	 * @throws SecurityException if the security manager doesn't allow access to
	 * the required constructor
	 * @throws NoSuchMethodException if the required constructor doesn't exit
	 * @throws IllegalArgumentException if the number of arguments provided to
	 * the constructor is wrong
	 * @throws InstantiationException if the required class is abstract
	 * @throws IllegalAccessException if the required constructor is
	 * inaccessible
	 * @throws InvocationTargetException if the invoked constructor raises an
	 * exception
	 */
	@SuppressWarnings("unchecked")	// because of (Set<E>) instance cast
	public static <E> Set<E> getSet(Class<? extends Set<E>> setType,
	                                Collection<? extends E> content) throws
		SecurityException,
	    NoSuchMethodException,
	    IllegalArgumentException,
	    InstantiationException,
	    IllegalAccessException,
	    InvocationTargetException
	{
		Constructor<? extends Set<E>> constructor = null;
		Class<?>[] argumentsTypes = null;
		Object[] arguments = null;
		Object instance = null;

		if (content == null)
		{
			argumentsTypes = new Class<?>[0];
			arguments = new Object[0];
		}
		else
		{
			argumentsTypes = new Class<?>[1];
			argumentsTypes[0] = Collection.class;
			arguments = new Object[1];
			arguments[0] = content;
		}

		constructor = setType.getConstructor(argumentsTypes);

		if (constructor != null)
		{
			instance = constructor.newInstance(arguments);
		}

		return (Set<E>) instance;
	}
}
