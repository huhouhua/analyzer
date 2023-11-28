package com.core.git.testdata;

import lombok.NonNull;
import org.ruijie.core.git.CredentialsConfigProvider;
import org.ruijie.core.git.GitProjectConfigProvider;

public class CloneConfigProvider extends GitProjectConfigProvider {

    private static final String   gitUrl="git@172.17.189.70:v-huhouhua/springdemo.git";
    private  static final String  cloneLocalPath="I:\\test\\project";
    private static  final String  branch="master";
    private   static final String  username="";
    private static  final String password="";

    public CloneConfigProvider(@NonNull String gitUrl, @NonNull String cloneLocalPath, @NonNull String branch, CredentialsConfigProvider credentialsConfigProvider) {
        super(gitUrl, cloneLocalPath, branch, credentialsConfigProvider);
    }

//    public CloneConfigProvider() {
//        super("","","","","");
//    }

}
