package org.ruijie.core.git;

import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.SshSessionFactory;

public class SshCredentialsConfig extends CredentialsConfigProvider {
    private SshSessionFactory sessionFactory;

    public SshCredentialsConfig(SshSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    @Override
    public Object getInstance() {
        return sessionFactory;
    }
}
