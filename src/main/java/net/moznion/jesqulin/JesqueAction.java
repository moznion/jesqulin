package net.moznion.jesqulin;

import java.util.Map;

/**
 * Action interface for Jesque.
 * <p>
 * The class which is implemented this is invoked from jesque worker.
 *
 * @param <T> Related {@link JesqueArgument}; Serialized instance of this class will be passed via {@code setArg()}.
 */
public interface JesqueAction<T extends JesqueArgument> extends Runnable {
    /**
     * Provides the name of queue for this action.
     *
     * @return The name of queue for this action
     */
    String getQueueName();

    /**
     * Setter of arguments.
     * <p>
     * This method will be invoked before {@code run()} invoking.
     * Argument of {@code arg} contains deserialized {@link JesqueArgument} as Map structure.
     * So you must checking the type of value from Map that you fetched.
     *
     * @param arg Argument for this action.
     */
    void setArg(Map<String, Object> arg);
}
