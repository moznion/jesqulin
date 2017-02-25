package net.moznion.jesqulin;

import net.greghaines.jesque.Job;

/**
 * A client class for Jesque.
 */
public interface JesqueClient {
    /**
     * Enqueues the job to specified queue.
     *
     * @param queueName The identifier of queue.
     * @param job       The job to enqueue.
     */
    void enqueue(String queueName, Job job);
}
