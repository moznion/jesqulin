package net.moznion.jesqulin;

/**
 * Argument interface for {@link JesqueAction}.
 * <p>
 * Serialized instance of the class that is implemented this interface will be passed to {@link JesqueAction}.
 * <p>
 * [NOTICE]
 * You must provide getter methods for each instance fields because Jesque uses Jackson as serializer internally.
 * Jackson requires getter methods to serialize the instance.
 */
public interface JesqueArgument {
}
