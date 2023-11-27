package com.ruijie.scheduler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TaskConfig {
   private  Global global;

//    @JsonProperty("project")
    private ProjectConfig[] project;

    public void setGlobal(@NonNull Global global) {
        this.global = global;
    }

    public void setProject(@NonNull ProjectConfig[] project) {
        this.project = project;
    }

    public Global getGlobal() {
      return  this.global;
    }

    public ProjectConfig[] getProject() {
      return  this.project;
    }

    @Override
    public String toString() {
        return "TaskConfig{" +
                "global=" + global +
                ", project=" + Arrays.toString(project) +
                '}';
    }
}
