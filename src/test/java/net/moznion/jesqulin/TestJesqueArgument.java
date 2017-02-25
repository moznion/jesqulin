package net.moznion.jesqulin;

public class TestJesqueArgument implements JesqueArgument {
    private final long id;

    public TestJesqueArgument(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
