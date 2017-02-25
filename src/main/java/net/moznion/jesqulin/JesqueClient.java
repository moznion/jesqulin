package net.moznion.jesqulin;

import net.greghaines.jesque.Job;

public interface JesqueClient {
    void enqueue(String queueType, Job job);
}
