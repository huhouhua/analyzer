package com.ruijie.core.docker;

import lombok.NonNull;

import java.util.Map;

public class ContainerArgsBuilder {

    private  String  imageName;

    private  String containerName;

    private Map<String,String>  envsMap;

    public ContainerArgsBuilder(@NonNull String imageName){
        this.imageName = imageName;
    }

    public static ContainerArgsBuilder newBuilder(@NonNull String imageName){
        return  new ContainerArgsBuilder(imageName);
    }

    public String getImageName() {
      return imageName;
    }

    public String getContainerName() {
       return  this.containerName;
    }

    public Map<String,String> getEnvsMap() {
       return  this.envsMap;
    }
    public ContainerArgsBuilder withImageName(@NonNull String imageName) {
        this.imageName = imageName;
        return  this;
    }

    public ContainerArgsBuilder addEnv(@NonNull String key, String value){
        if (!containsEnvKey(key)){
            this.envsMap.put(key,value);
        }
        return  this;
    }
    public ContainerArgsBuilder removeEnv(@NonNull String key, String value){
        if (containsEnvKey(key)){
            this.envsMap.remove(key);
        }
        return  this;
    }
    public  boolean containsEnvKey(@NonNull String key){
        return  this.envsMap.containsKey(key);
    }


    public ContainerArgsBuilder withContainerName(@NonNull String containerName) {
        this.containerName = containerName;
        return  this;
    }

    public ContainerArgsBuilder withEnvsMap(@NonNull Map<String, String> envsMap) {
        this.envsMap = envsMap;
        return  this;
    }

    @Override
    public String toString() {
        return "ContainerArgsBuilder{" +
                "imageName='" + imageName + '\'' +
                ", containerName='" + containerName + '\'' +
                ", envsMap=" + envsMap +
                '}';
    }
}
