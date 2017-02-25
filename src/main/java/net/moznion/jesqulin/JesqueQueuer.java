package net.moznion.jesqulin;

/**
 * Queuer class for Jesque.
 *
 * @param <T>
 * @param <Y>
 */
public class JesqueQueuer<T extends JesqueArgument, Y extends JesqueAction<T>> {
    private final Class<Y> actionClass;
    private final JesqueClient jesqueClient;
    private final String queueName;

    public JesqueQueuer(final Class<Y> actionClass,
                        final JesqueClient jesqueClient) throws IllegalAccessException, InstantiationException {
        this.actionClass = actionClass;
        this.jesqueClient = jesqueClient;

        final JesqueAction action = actionClass.newInstance();
        queueName = action.getQueueName();
    }

    /**
     * Enqueues the job to Jesqueue's queue.
     * <p>
     * Passed argument will be transformed to Job of Jesque.
     *
     * @param argument The argument that represents job.
     */
    public void enqueue(final T argument) {
        jesqueClient.enqueue(
                queueName,
                new JesqueJobFactory<T, Y>().create(actionClass, argument)
        );
    }
}
