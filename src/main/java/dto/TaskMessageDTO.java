package dto;

import java.io.Serializable;

public class TaskMessageDTO implements Serializable {
    public String username;
    public String taskType;
    public long executeAtEpochMillis;
}
