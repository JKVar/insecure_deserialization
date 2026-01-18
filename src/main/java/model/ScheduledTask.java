package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class ScheduledTask implements Serializable, Comparable<ScheduledTask> {

    private final LocalDateTime executeAt;
    private final String username;
    private final Task task;

    public ScheduledTask(LocalDateTime executeAt, String username, Task task) {
        this.executeAt = executeAt;
        this.username = username;
        this.task = task;
    }

    public LocalDateTime getExecuteAt() {
        return executeAt;
    }

    public Task getTask() {
        return task;
    }

    public String getOwner() {
        return username;
    }

    @Override
    public int compareTo(ScheduledTask o) {
        return Long.compare(this.executeAt.toEpochSecond(ZoneOffset.MIN), o.executeAt.toEpochSecond(ZoneOffset.MIN));
    }
}
