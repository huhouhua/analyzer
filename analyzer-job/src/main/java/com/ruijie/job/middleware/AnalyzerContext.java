package com.ruijie.job.middleware;

import com.ruijie.core.Context;
import com.ruijie.job.config.ProjectConfigProvider;
import com.ruijie.job.config.SonarConfigProvider;

public class AnalyzerContext implements Context {
    private final ProjectConfigProvider projectConfigProvider;
    private final SonarConfigProvider sonarConfigProvider;

    public AnalyzerContext(ProjectConfigProvider projectConfigProvider,
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
