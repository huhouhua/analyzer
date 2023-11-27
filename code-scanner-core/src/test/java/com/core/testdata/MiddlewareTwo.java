package com.core.testdata;

import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;

public class MiddlewareTwo implements Middleware<TestContext> {

    @Override
    public Object invoke(Object prev, TestContext ctx, MiddlewareNext next) {
        System.out.println("prev result:"+prev.toString());
        System.out.println("Two....");
        System.out.println("call next");
        Object result = next.execute("MiddlewareTwo");
        System.out.println("call next complete");
        System.out.println("next result:"+result.toString());
        return "MiddlewareTwo call complete....";
    }
}
