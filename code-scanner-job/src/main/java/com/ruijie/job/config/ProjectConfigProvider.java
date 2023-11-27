package com.ruijie.job.config;

import org.ruijie.core.ProjectConfigContract;
import org.springframework.stereotype.Component;

public class ProjectConfigProvider {
    public  static String getRepoUrl(){
//        return  "http://172.17.189.70/riil-insight/riil-insight-appserver.git";

   return  System.getenv(ProjectConfigContract.REPO_URL_TAG);

    }
    public  static String getBranch(){
//        return  "develop";
        return  System.getenv(ProjectConfigContract.BRANCH_TAG);
    }
    public static   String getSonarFileUrl(){
//        return  "release\\docker\\SonarDockerfile";
      return  System.getenv(ProjectConfigContract.SONAR_FILE_URL_TAG);
    }
    public  static String getMode(){
//        return  "Dockerfile";
      return  System.getenv(ProjectConfigContract.SONAR_MODE_TAG);
    }
    public  static String getDescription(){
//        return  "appserver";
    return  System.getenv(ProjectConfigContract.PROJECT_DESCRIPTION_TAG);
    }

    public  static String getRepoName(){
//        return  "appserver";
        return  System.getenv(ProjectConfigContract.REPO_NAME);
    }
}
