package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ProjectConfig {

    private ArrayList<Group> groups;
    private  String name;
    private  Integer  parallel;
    private  String  description;


    public void setName(@NonNull String name) {
        this.name = name;
    }

    public void setParallel( Integer parallel) {
        this.parallel = parallel;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    public void setGroups(@NonNull ArrayList<Group> groups) {
        this.groups = groups;
    }
    public String getName() {
      return this.name;
    }

    public Integer getParallel() {
        return this.parallel;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Group> getGroups() {
       return this.groups;
    }

    @Override
    public String toString() {
        return "ProjectConfig{" +
                "name='" + name + '\'' +
                ", parallel=" + parallel +
                ", description='" + description + '\'' +
                ", groups=" + groups +
                '}';
    }

}
