package com.tt.test.handler;

import com.tt.message.rpc.test.ITest;
import org.springframework.stereotype.Component;

@Component
public class TestRpc implements ITest {
    @Override
    public boolean sayHello(String value) {
        return true;
    }
}
