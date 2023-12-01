package com.ruijie.core.git;

import lombok.NonNull;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class UsernamePasswordCredentialsConfig extends CredentialsConfigProvider {
    private final UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider;

    public UsernamePasswordCredentialsConfig(@NonNull String username, @NonNull String password) {
        usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
    }

    public UsernamePasswordCredentialsConfig(@NonNull UsernamePasswordCredentialsProvider provider) {
        usernamePasswordCredentialsProvider = provider;
    }
    @Override
    public Object getInstance() {
        return usernamePasswordCredentialsProvider;
    }
}
