package net.moznion.jesqulin;

public class JesqueQueuer<T extends JesqueArgument, Y extends JesqueAction<T>> {
    private final Class<Y> actionClass;
    private final JesqueClient jesqueClient;
    private final String queueType;

    public JesqueQueuer(final Class<Y> actionClass,
                        final JesqueClient jesqueClient) throws IllegalAccessException, InstantiationException {
        this.actionClass = actionClass;
        this.jesqueClient = jesqueClient;

        final JesqueAction action = actionClass.newInstance();
        queueType = action.getQueueName();
    }

    public void enqueue(final T argument) {
        jesqueClient.enqueue(
                queueType,
                new JesqueJobFactory<T, Y>().create(actionClass, argument)
        );
    }
}
