package org.ruijie.core.git;

public abstract class CredentialsConfigProvider {
    public abstract Object getInstance();

    public <T> T getCredentialsConfig(Class<T> providerType) {
        Object instance = this.getInstance();
        if (providerType.isInstance(instance)) {
            return providerType.cast(instance);
        }
        return null;
    }
}
