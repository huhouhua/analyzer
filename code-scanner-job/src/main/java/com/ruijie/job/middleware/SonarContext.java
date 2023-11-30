package com.ruijie.job.middleware;

import com.ruijie.job.config.ProjectConfigProvider;
import com.ruijie.job.config.SonarConfigProvider;
import org.ruijie.core.Context;

public class SonarContext implements Context {
    private final ProjectConfigProvider projectConfigProvider;
    private final SonarConfigProvider sonarConfigProvider;

    public SonarContext(ProjectConfigProvider projectConfigProvider,
                        SonarConfigProvider sonarConfigProvider) {
        this.projectConfigProvider = projectConfigProvider;
        this.sonarConfigProvider = sonarConfigProvider;
        this.setUp();

    }
    public ProjectConfigProvider getProject() {
        return this.projectConfigProvider;
    }

    public SonarConfigProvider getSonar() {
        return this.sonarConfigProvider;
    }
    public void  setUp(){
      this.sonarConfigProvider.setBranch(this.projectConfigProvider.getBranch());
    }
}
