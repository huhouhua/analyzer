package com.core;

import com.core.testdata.MiddlewareOne;
import com.core.testdata.MiddlewareThree;
import com.core.testdata.MiddlewareTwo;
import com.core.testdata.TestContext;
import com.ruijie.core.MiddlewareChain;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.function.Consumer;

public class MiddlewareTest {

    private  static Consumer<TestContext> middleware;

    @BeforeClass
    public  static  void  setup(){
        middleware = new MiddlewareChain<TestContext>().
                use(new MiddlewareOne()).
                use(new MiddlewareTwo()).
                use(new MiddlewareThree()).
                build();
    }
    @Test
    public void TestMiddleware(){
        middleware.accept(new TestContext());
    }
}
