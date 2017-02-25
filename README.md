jesqulin [![Build Status](https://travis-ci.org/moznion/jesqulin.svg?branch=master)](https://travis-ci.org/moznion/jesqulin)
========

A framework to provide a safe way to use [Jesque](https://github.com/gresrun/jesque).

Getting started
--

### Prepare classes

#### Argument class

Argument class is the payload of Jesque and this class is related to action class.

```java
public class SampleArgument implements JesqueArgument {
    private final long id;

    public SampleArgument(final long id) {
        this.id = id;
    }

    // This getter is necessary to serialize by Jackson
    // (Jesque uses Jackson internally)
    public long getId() {
        return id;
    }
}
```

#### Action class

Action class is the core of job. Worker of Jesque invokes this runnable action.

```java
public class SampleAction implements JesqueAction<SampleArgument> {
    private SampleArgument arg;

    @Override
    public String getQueueName() {
        return "sample-queue";
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

        this.arg = new SampleArgument(id);
    }
}
```

### Client side

#### Create Jesque client

```java
public class SampleJesqueClient implements JesqueClient {
    private final Client clientPool;
    private final Pool<Jedis> jedisPool;

    public SampleJesqueClient(final String host, final int port, final String namespace) {
        final Config jesqueConfig = new ConfigBuilder()
                .withNamespace(namespace)
                .withHost(host)
                .withPort(port)
                .build();
        jedisPool = PoolUtils.createJedisPool(jesqueConfig);
        clientPool = new ClientPoolImpl(jesqueConfig, jedisPool);
    }

    @Override
    public void enqueue(String queueName, Job job) {
        clientPool.enqueue(queueName, job);
    }

    public Pool<Jedis> getJedisPool() {
        return jedisPool;
    }
}
```

#### Enqueue job (argument)

```java
SampleJesqueClient jesqueClient = new SampleJesqueClient("127.0.0.1", 6379, "namespace");
final JesqueQueuer<SampleArgument, SampleAction> queuer =
    new JesqueQueuer<>(SampleAction.class, jesqueClient);
queuer.enqueue(new SampleArgument(123));
```

### Worker side

```java
// Register queue and create job factory
final List<? extends JesqueAction> actions = Arrays.asList(
        new SampleAction()
);
Map<String, Class<? extends JesqueAction>> workingClassMap = new HashMap<>(actions.size());
for (final JesqueAction action : actions) {
    final Class<? extends JesqueAction> clazz = action.getClass();
    workingClassMap.put(clazz.getName(), clazz);
}
final Set<String> queues = actions.stream().map(JesqueAction::getQueueName).collect(Collectors.toSet());

final Config jesqueConfig = new ConfigBuilder()
        .withNamespace("namespace")
        .withHost("127.0.0.1")
        .withPort(6379)
        .build();
final Pool<Jedis> jedisPool = PoolUtils.createJedisPool(jesqueConfig);

final WorkerPoolImpl workerPool = new WorkerPoolImpl(
        new ConfigBuilder().withHost("127.0.0.1").withPort(6379).withNamespace("namespace").build(),
        queues,
        new MapBasedJobFactory(workingClassMap),
        jedisPool
);

workerPool.run();
```

Behavior and Mechanism
--

### Client side

- Enqueues an instance of the class that implements `JesqueArgument` via `JesqueQueuer`
- `JesqueArgument` is related to `JesqueAction`
- `JesqueAction` has the identifier of queue (i.e. `queueName`)
- `JesqueQueuer` enqueues `JesqueArgument` to queue that is identified by `queueName`
- When enqueuing, instance of the argument class is serialized to JSON by Jackson

### Worker side

- Jesque worker dequeues payload (i.e. serialized JSON string) from any queue
- The queue is related to `JesqueAction`; worker instantiate which action
- Deserialize payload by `JesqueArgument` class that is related to `JesqueAction`
    - And pass this deserialized object as Map structure to instantiated `JesqueArgument` via `setArg()` method
- Invoke `run()` method of `JesqueAction` after `setArg()` is called

Author
--

moznion (<moznion@gmail.com>)

License
--

```
The MIT License (MIT)
Copyright © 2017 moznion, http://moznion.net/ <moznion@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the “Software”), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```

