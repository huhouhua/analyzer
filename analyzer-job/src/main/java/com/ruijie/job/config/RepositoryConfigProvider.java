package com.ruijie.job.config;

import com.ruijie.core.git.GitConfigSessionFactory;
import lombok.NonNull;
import org.eclipse.jgit.transport.SshSessionFactory;

import java.io.FileNotFoundException;

public class RepositoryConfigProvider {
    public static void initGitConfig(@NonNull GitRepositoryConfig gitRepository) throws FileNotFoundException {
        SshSessionFactory.setInstance(new GitConfigSessionFactory(gitRepository.getPrivateKeyFilePath()));
    }
    public static SshSessionFactory getSshSessionFactory() {
        return SshSessionFactory.getInstance();
    }
}
