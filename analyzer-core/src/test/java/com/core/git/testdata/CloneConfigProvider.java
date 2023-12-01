package com.core.git.testdata;

import com.ruijie.core.git.CredentialsConfigProvider;
import com.ruijie.core.git.GitProjectConfigProvider;
import lombok.NonNull;

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
