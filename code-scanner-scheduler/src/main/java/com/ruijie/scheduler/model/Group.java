package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Group {
  private  String name;
  private  String mode;
  private  String  description;
  private  ProjectRepository  repo;

  public void setName(@NonNull String name) {
    this.name = name;
  }

  public void setMode(@NonNull String mode) {
    this.mode = mode;
  }

  public void setDescription( String description) {
    this.description = description;
  }

  public void setRepo(@NonNull ProjectRepository repo) {
    this.repo = repo;
  }

  public String getName() {
    return this.name;
  }

  public String getMode() {
    return this.mode;
  }

  public String getDescription() {
     return  this.description;
  }

  public ProjectRepository getRepo() {
    return  this.repo;
  }

  @Override
  public String toString() {
    return "Group{" +
            "name='" + name + '\'' +
            ", mode='" + mode + '\'' +
            ", description='" + description + '\'' +
            ", repo=" + repo +
            '}';
  }

}
