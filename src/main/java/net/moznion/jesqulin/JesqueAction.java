package net.moznion.jesqulin;

import java.util.Map;

public interface JesqueAction<T extends JesqueArgument> extends Runnable {
    String getQueueType();

    void setArg(Map<String, Object> arg);
}
