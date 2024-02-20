package com.tt.message.rpc.test;

import com.tt.message.anno.Message;
import com.tt.message.constant.MessageIdStartConst;
import com.tt.message.rpc.IRpc;

import java.util.concurrent.CompletableFuture;

@Message(startId = MessageIdStartConst.TEST_1)
public interface ITest1 extends IRpc {

    String strTest(int index);

    CompletableFuture<String> asyncTest(int index);
}
