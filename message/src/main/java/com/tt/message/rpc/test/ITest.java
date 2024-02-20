package com.tt.message.rpc.test;

import com.tt.message.anno.Message;
import com.tt.message.constant.MessageIdStartConst;
import com.tt.message.rpc.IRpc;

@Message(startId = MessageIdStartConst.TEST)
public interface ITest extends IRpc {

    boolean sayHello(String value);
}
