package net.moznion.jesqulin;

import java.util.Map;

public interface JesqueAction<T extends JesqueArgument> extends Runnable {
    String getQueueName();

    void setArg(Map<String, Object> arg);
}
