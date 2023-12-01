package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TaskConfig {
   private  Global global;

//    @JsonProperty("project")
    private Group[] groups;

    public void setGlobal(@NonNull Global global) {
        this.global = global;
    }

    public void setGroups(@NonNull Group[] groups) {
        this.groups = groups;
    }

    public Global getGlobal() {
      return  this.global;
    }

    public Group[] getGroups() {
      return  this.groups;
    }

    @Override
    public String toString() {
        return "TaskConfig{" +
                "global=" + global +
                ", groups=" + Arrays.toString(groups) +
                '}';
    }
}
