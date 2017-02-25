package net.moznion.jesqulin;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.utils.PoolUtils;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class TestJesqueClient implements JesqueClient {
    private final Client clientPool;
    private final Pool<Jedis> jedisPool;


    public TestJesqueClient(final String host, final int port, final String namespace) {
        final Config jesqueConfig = new ConfigBuilder()
                .withNamespace(namespace)
                .withHost(host)
                .withPort(port)
                .build();
        jedisPool = PoolUtils.createJedisPool(jesqueConfig);
        clientPool = new ClientPoolImpl(jesqueConfig, jedisPool);
    }

    @Override
    public void enqueue(String queueType, Job job) {
        clientPool.enqueue(queueType, job);
    }

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }
}
