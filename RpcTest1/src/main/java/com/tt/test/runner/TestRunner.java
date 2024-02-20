package com.tt.test.runner;

import com.tt.message.rpc.test.ITest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class TestRunner implements ApplicationRunner {

    @Autowired
    @Qualifier("ITestSendProxy")
    ITest test;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Thread.sleep(5000);

        boolean hello = test.sayHello("hello");
        System.out.println(hello);
    }
}
