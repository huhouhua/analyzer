package org.ruijie.core;

@FunctionalInterface
public interface Middleware<TContext extends Context> {
    Object invoke(Object prev, TContext ctx, MiddlewareNext next) throws Exception;
}