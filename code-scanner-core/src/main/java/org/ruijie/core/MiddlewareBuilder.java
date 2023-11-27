package org.ruijie.core;


import java.util.function.Consumer;


public  interface MiddlewareBuilder<TContext extends Context>  {
    MiddlewareBuilder<TContext> use(Middleware<TContext> middleware);
    Consumer<TContext> build();
}