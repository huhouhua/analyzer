package com.ruijie.core.docker;

import cn.hutool.core.util.StrUtil;
import com.ruijie.core.SonarConfig;

import java.util.regex.Matcher;

class DockerSonarArgsBuilder {
    private final SonarConfig sonarConfig;
    private static final String DOCKER_FILE_REPLAC_VARIABLE = "-Dsonar.branch.name";
    private static final String DOCKER_FILE_URL_VARIABLE = "-Dsonar.host.url";
    private static final String DOCKER_FILE_LOGIN_VARIABLE = "-Dsonar.login";
    private static final String DOCKER_FILE_PASSWORD_VARIABLE = "-Dsonar.password";

    private final StringBuilder builder;
    public DockerSonarArgsBuilder(SonarConfig sonarConfig) {
        this.sonarConfig = sonarConfig;
        this.builder = new StringBuilder(40);
    }

    public static DockerSonarArgsBuilder newBuilder(SonarConfig sonarConfig) {
        return new DockerSonarArgsBuilder(sonarConfig);
    }

    public String build() {
        return builder.toString();
    }

    public String buildOfAll() {
        return this.withBranch().
                withUrl().
                withLogin().
                withPassword().
                build();
    }

    public DockerSonarArgsBuilder withBranch() {
        if (sonarConfig.getBranch().isEmpty()) {
            throw new NullPointerException("The branch cannot be empty!");
        }
        builder.append(StrUtil.format("  {}={}",DOCKER_FILE_REPLAC_VARIABLE,sonarConfig.getBranch()));
        return this;
    }

    public DockerSonarArgsBuilder withUrl() {
        if (!sonarConfig.getEnable()){
            return this;
        }
        if (sonarConfig.getUrl().isEmpty()) {
            throw new NullPointerException("The url cannot be empty!");
        }
        builder.append(StrUtil.format("  {}={}",DOCKER_FILE_URL_VARIABLE,sonarConfig.getUrl()));
        return this;
    }

    public DockerSonarArgsBuilder withLogin() {
        if (!sonarConfig.getEnable()){
            return this;
        }
        if (sonarConfig.getLogin().isEmpty()) {
            throw new NullPointerException("The login cannot be empty!");
        }
        builder.append(StrUtil.format("  {}={}",DOCKER_FILE_LOGIN_VARIABLE,sonarConfig.getLogin()));
        return this;
    }
     public DockerSonarArgsBuilder withPassword() {
         if (!sonarConfig.getEnable()){
             return this;
         }
         if (sonarConfig.getPassword().isEmpty()) {
             throw new NullPointerException("The password cannot be empty!");
         }
         builder.append(StrUtil.format("  {}={}",DOCKER_FILE_PASSWORD_VARIABLE, Matcher.quoteReplacement(sonarConfig.getPassword())));
         return this;
     }
}
