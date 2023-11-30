package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Group {

    private ArrayList<ProjectConfig> projects;
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

    public void setProjects(@NonNull ArrayList<ProjectConfig> projects) {
        this.projects = projects;
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

    public ArrayList<ProjectConfig> getProjects() {
       return this.projects;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", parallel=" + parallel +
                ", description='" + description + '\'' +
                ", projects=" + projects +
                '}';
    }

}
