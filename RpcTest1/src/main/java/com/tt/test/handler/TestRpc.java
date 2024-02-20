package com.tt.test.handler;

import com.tt.message.rpc.test.ITest1;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class TestRpc implements ITest1 {

    static final String[] arr = new String[10];
    static {
        for (int i = 0; i < 10; i++) {
            arr[i] = String.valueOf(i);
        }
    }

    @Override
    public String strTest(int index) {
        if (index < 0 || index >= arr.length) {
            return null;
        }
        return arr[index];
    }

    @Override
    public CompletableFuture<String> asyncTest(int index) {
        return returnAsyncObj(strTest(index));
    }
}
