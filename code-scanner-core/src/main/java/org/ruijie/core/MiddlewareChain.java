package org.ruijie.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class MiddlewareChain<TContext extends Context> implements MiddlewareBuilder<TContext> {
    private final List<Middleware<TContext>> middlewareList;
    public MiddlewareChain() {
        this.middlewareList = new ArrayList<>();
    }
    public MiddlewareBuilder<TContext> use(Middleware<TContext> middleware) {
        this.middlewareList.add(middleware);
        return this;
    }
    public Consumer<TContext> build() {
        Pipeline<TContext> pipeline = null;
        Collections.reverse(middlewareList);
        for (Middleware<TContext> middleware : middlewareList) {
            pipeline = pipeline == null ? new Pipeline<>(middleware) :
                    pipeline.addHandler(middleware);
        }
        assert pipeline != null;
        return pipeline::invoke;

    }

    private static class Pipeline<TContext extends Context> {
        private final Middleware<TContext> currentHandler;

        public Pipeline(Middleware<TContext> currentHandler) {
            this.currentHandler = currentHandler;
        }

        public Pipeline<TContext> addHandler(Middleware<TContext> newHandler) {
            return new Pipeline<TContext>((prev, ctx, next) -> {
                MiddlewareNext newNext = (Object prev2) -> {
                    try {
                        return currentHandler.invoke(prev2, ctx, next);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                };
                return newHandler.invoke(prev, ctx, newNext);
            });
        }
        public void invoke(TContext ctx)  {
            try {
                this.currentHandler.invoke(null, ctx, (Object prev) -> {
                    return null;
                });
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
    }
}