package com.ruijie.scheduler.model;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class Global {
     private Sonar sonar;
     private Integer parallel;
     private String triggerTimeCron;
     private GlobalRepository repo;

     public void setSonar(@NonNull Sonar sonar) {
          this.sonar = sonar;
     }

     public void setParallel(@NonNull Integer parallel) {
          this.parallel = parallel;
     }

     public void setTriggerTimeCron(@NonNull String triggerTimeCron) {
          this.triggerTimeCron = triggerTimeCron;
     }

     public void setRepo(@NonNull GlobalRepository repo) {
          this.repo = repo;
     }

     public Sonar getSonar() {
          return this.sonar;
     }

     public Integer getParallel() {
          return this.parallel;
     }

     public String getTriggerTimeCron() {
          return this.triggerTimeCron;
     }

     public GlobalRepository getRepo() {
          return this.repo;
     }

     @Override
     public String toString() {
          return "Global{" +
                  "sonar=" + sonar +
                  ", parallel=" + parallel +
                  ", triggerTimeCron='" + triggerTimeCron + '\'' +
                  ", repo=" + repo +
                  '}';
     }
}
