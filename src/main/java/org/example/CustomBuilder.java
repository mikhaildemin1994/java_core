package org.example;

import java.util.Stack;

public final class CustomBuilder {
    private final Stack<String> snapshots;
    private final StringBuilder builder;

    public CustomBuilder() {
        this.snapshots = new Stack<>();
        this.builder = new StringBuilder();
    }

    public CustomBuilder append(String str) {
        snapshots.push(str);
        builder.append(str);
        return this;
    }

    public CustomBuilder undo() {
        if(!snapshots.isEmpty()) {
            builder.append(snapshots.pop());
        }
        return this;
    }

    @Override
    public String toString() {
        return "CustomBuilder{" +
                "snapshots=" + snapshots +
                ", builder=" + builder +
                '}';
    }
}
