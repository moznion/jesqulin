package net.moznion.jesqulin;

import net.greghaines.jesque.Job;

import java.util.HashMap;
import java.util.Map;

class JesqueJobFactory<T extends JesqueArgument, U extends JesqueAction<T>> {
    Job create(final Class<U> actionClass, final T argument) {
        final Map<String, JesqueArgument> vars = new HashMap<>();
        vars.put("arg", argument);

        return new Job(actionClass.getName(), vars);
    }
}
