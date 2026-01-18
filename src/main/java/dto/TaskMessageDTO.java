package dto;

import java.io.Serializable;

public class TaskMessageDTO implements Serializable {
    public String username;          // only username, not full User object
    public String taskType;
    public long executeAtEpochMillis;
}
