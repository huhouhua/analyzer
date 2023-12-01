package com.ruijie.core;

public class MiddlewareFactory {
    public  static MiddlewareBuilder<Context> CreateDefault(){
        return  new MiddlewareChain<>();
    }

    public  static <TContext extends Context> MiddlewareBuilder<TContext> CreateDefaultWithGeneric(){
        return  new MiddlewareChain<>();
    }
}
