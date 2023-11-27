package com.core.testdata;


import org.ruijie.core.Middleware;
import org.ruijie.core.MiddlewareNext;

public class MiddlewareThree  implements Middleware<TestContext> {
    @Override
    public Object invoke(Object prev, TestContext ctx, MiddlewareNext next) {
        System.out.println("start MiddlewareThree");
        System.out.println("prev result:"+prev.toString());
        System.out.println("three....");
        System.out.println("call next");
         next.execute("MiddlewareThree");
        System.out.println("call next complete");
        return "MiddlewareThree call complete....";
    }
}
