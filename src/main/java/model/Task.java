package model;

import java.io.Serializable;

public interface Task extends Serializable {
    void execute();
    void setParam(Object param);
}

