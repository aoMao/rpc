package com.tt.core.message;

import com.tt.anno.RpcQueueKey;
import com.tt.core.net.handler.queuekey.DefaultRpcQueueKeyAlgo;
import com.tt.core.net.handler.queuekey.IRpcQueueKeyAlgo;
import com.tt.core.net.proxy.RpcRequestProxy;
import com.tt.core.util.ReflectionUtil;
import com.tt.message.anno.LoadBalance;
import com.tt.message.constant.LBType;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

/**
 * rpc消息定义
 */
public class MessageEntry {
    private final int id;
    private final Method method;
    private final Object instance;
    private final String methodName;
    private final Type[] paramClzArray;
    private final LBType lbType;
    private final Class<? extends IRpcQueueKeyAlgo> rpcQueueAlgoClz;
    private final boolean async;
    private final boolean resultMsg;
    private final boolean canDeal;

    @SuppressWarnings("unchecked")
    public MessageEntry(int id, Method method, Object instance, boolean resultMsg) {
        this.id = id;
        this.method = method;
        this.instance = instance;
        methodName = method.getName();
        if (resultMsg) {
            paramClzArray = new Type[]{ReflectionUtil.getTypeOrFirstParamType(method.getGenericReturnType(), CompletableFuture.class)};
        } else {
            paramClzArray = method.getGenericParameterTypes();
        }
        if (resultMsg) {
            this.lbType = LBType.SERVER_ID;
        } else {
            LoadBalance loadBalance = ReflectionUtil.getAnno(method, LoadBalance.class);
            this.lbType = loadBalance == null ? LBType.CONSISTENT_HASH : loadBalance.lbType();
        }
        var rpcQueueKey = ReflectionUtil.getAnno(method, RpcQueueKey.class);
        if (rpcQueueKey == null) {
            rpcQueueAlgoClz = DefaultRpcQueueKeyAlgo.class;
        } else {
            // 此处可能有报错，不过只要运行到就会报错
            this.rpcQueueAlgoClz = (Class<? extends IRpcQueueKeyAlgo>) rpcQueueKey.value();
        }
        async = CompletableFuture.class.isAssignableFrom(method.getReturnType());
        this.resultMsg = resultMsg;
        canDeal = instance != null && !(instance instanceof Proxy && Proxy.getInvocationHandler(instance) instanceof RpcRequestProxy);
    }

    public int getId() {
        return id;
    }

    public String getMessageName() {
        return methodName;
    }

    public Method getMethod() {
        return method;
    }

    public Object getHandlerInstance() {
        return instance;
    }

    public Type[] getParamTypes() {
        return paramClzArray;
    }

    public LBType lbType() {
        return lbType;
    }

    public Class<? extends IRpcQueueKeyAlgo> getRpcQueueAlgoClz() {
        return rpcQueueAlgoClz;
    }

    public boolean isAsync() {
        return async;
    }

    public boolean isResultMsg() {
        return resultMsg;
    }

    public boolean canDeal() {
        return canDeal;
    }
}
