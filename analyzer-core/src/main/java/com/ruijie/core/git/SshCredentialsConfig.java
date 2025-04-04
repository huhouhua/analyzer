package com.ruijie.core.git;

import org.eclipse.jgit.transport.SshSessionFactory;

public class SshCredentialsConfig extends CredentialsConfigProvider {
    private final SshSessionFactory sessionFactory;

    public SshCredentialsConfig(SshSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Object getInstance() {
        return sessionFactory;
    }
}
