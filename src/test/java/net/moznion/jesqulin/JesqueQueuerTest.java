package net.moznion.jesqulin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.greghaines.jesque.utils.JesqueUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static net.greghaines.jesque.utils.ResqueConstants.QUEUE;
import static org.assertj.core.api.Assertions.assertThat;

public class JesqueQueuerTest {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;

    private static TestJesqueClient jesqueClient;
    private static JesqueQueuer<TestJesqueArgument, TestJesqueAction> queuer;

    @BeforeClass
    public static void setupClass() throws InstantiationException, IllegalAccessException {
        jesqueClient = new TestJesqueClient(HOST, PORT, "namespace");
        queuer = new JesqueQueuer<>(TestJesqueAction.class, jesqueClient);
    }

    @Before
    public void setup() {
        try (final Jedis jedis = jesqueClient.getJedisPool().getResource()) {
            jedis.flushAll(); // initialize queue
        }
    }

    @Test
    public void shouldEnqueueSuccessfully() throws Exception {
        queuer.enqueue(new TestJesqueArgument(123));

        final String key = JesqueUtils.createKey("namespace", QUEUE, new TestJesqueAction().getQueueName());

        try (final Jedis jedis = jesqueClient.getJedisPool().getResource()) {
            assertThat(jedis.llen(key)).isEqualTo(1);
            final String payloadJSON = jedis.lpop(key);

            final ObjectMapper objectMapper = new ObjectMapper();
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
            };
            final Map<String, Object> job = objectMapper.readValue(payloadJSON, typeRef);

            //noinspection unchecked
            final Map<String, Object> vars = (Map<String, Object>) job.get("vars");
            //noinspection unchecked
            final Map<String, Object> arg = (Map<String, Object>) vars.get("arg");
            assertThat(arg.get("id")).isEqualTo(123);
        }
    }
}
