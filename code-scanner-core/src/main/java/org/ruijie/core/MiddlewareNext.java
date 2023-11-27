package org.ruijie.core;

@FunctionalInterface
public interface MiddlewareNext {
    Object execute(Object prev);
}