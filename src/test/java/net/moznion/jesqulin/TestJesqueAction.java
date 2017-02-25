package net.moznion.jesqulin;

import java.util.Map;

public class TestJesqueAction implements JesqueAction<TestJesqueArgument> {
    private TestJesqueArgument arg;

    @Override
    public String getQueueType() {
        return "test-queue";
    }

    @Override
    public void run() {
        System.out.println(arg.getId());
    }

    @Override
    public void setArg(Map<String, Object> arg) {
        final long id;
        {
            final Object item = arg.get("id");
            if (!(item instanceof Integer) && !(item instanceof Long)) {
                throw new IllegalArgumentException("id must not be null. And which must be integer or long value");
            }
            id = ((Number) item).longValue();
        }

        this.arg = new TestJesqueArgument(id);
    }
}
