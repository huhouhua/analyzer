package com.core.testdata;

import com.ruijie.core.Middleware;
import com.ruijie.core.MiddlewareNext;

public class MiddlewareOne implements Middleware<TestContext> {
    @Override
    public Object invoke(Object prev, TestContext ctx, MiddlewareNext next) {
        System.out.println("One....");
        System.out.println("call next");
        Object result = next.execute("MiddlewareOne ");
        System.out.println("call next complete");
        System.out.println("next result"+result.toString());
        return "MiddlewareOne call complete....";
    }
}
