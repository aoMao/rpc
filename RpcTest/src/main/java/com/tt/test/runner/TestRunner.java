package com.tt.test.runner;

import com.tt.message.rpc.test.ITest1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class TestRunner implements CommandLineRunner {

    @Autowired
    @Qualifier("ITest1SendProxy")
    ITest1 test1;

    @Override
    public void run(String... args) throws Exception {

        Thread.sleep(1000);

//        for (int i = -10; i < 12; i++) {
//            String s = test1.strTest(i);
//            System.out.println(s);
//        }

        for (int i = -10; i < 12; i++) {
            test1.asyncTest(i).whenComplete((r, t) -> {
                System.out.println(r);
            });
        }
    }
}
