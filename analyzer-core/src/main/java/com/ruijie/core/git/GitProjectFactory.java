package com.ruijie.core.git;
import lombok.NonNull;

public class GitProjectFactory {
    public  static   GitProjectSupport createFeatureInstance(@NonNull GitProjectConfigProvider gitProjectConfigProvider){
        return  new ProjectFeature(gitProjectConfigProvider);
    }
}
