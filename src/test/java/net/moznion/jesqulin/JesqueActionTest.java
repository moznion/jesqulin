package net.moznion.jesqulin;

import net.moznion.capture.output.stream.StdoutCapturer;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class JesqueActionTest {
    @Test
    public void shouldActionSuccessfully() {
        // Emulate behavior of jesque
        final TestJesqueAction testJesqueAction = new TestJesqueAction();
        final Map<String, Object> arg = new HashMap<>();
        arg.put("id", 123);
        testJesqueAction.setArg(arg);

        final ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        try (final StdoutCapturer capturer = new StdoutCapturer(stdout)) {
            testJesqueAction.run();
        }

        assertThat(stdout.toString()).isEqualTo("123\n");
    }
}
