package com.ruijie.scheduler.config;

import com.ruijie.core.git.GitConfigSessionFactory;
import com.ruijie.scheduler.model.GitRepository;
import com.ruijie.scheduler.model.RepositoryConfig;
import lombok.NonNull;
import org.eclipse.jgit.transport.SshSessionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;

@Configuration
public class RepositoryConfigProvider {

    @Bean
    @ConfigurationProperties(prefix = "git-repository")
    public RepositoryConfig getConfig() {
        return new RepositoryConfig();
    }


    public static void initGitConfig(@NonNull GitRepository gitRepository) throws FileNotFoundException {
        SshSessionFactory.setInstance(new GitConfigSessionFactory(gitRepository.getPrivateKeyFilePath()));
    }
    public static SshSessionFactory getSshSessionFactory() {
        return SshSessionFactory.getInstance();
    }
}
