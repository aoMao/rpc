package com.tt.core.net.util;

import com.tt.core.net.message.RpcMsg;
import com.tt.core.net.session.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionThreadLocalUtil {

    @SuppressWarnings("rawtypes")
    private static final List<ThreadLocal> all = new ArrayList<>();

    private static final ThreadLocal<Session> sessionThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<RpcMsg> msgThreadLocal = new ThreadLocal<>();

    static {
        all.add(sessionThreadLocal);
        all.add(msgThreadLocal);
    }

    public static void setSessionThreadLocal(Session session) {
        sessionThreadLocal.set(session);
    }

    public static Session getSession() {
        return sessionThreadLocal.get();
    }

    public static void setMsgThreadLocal(RpcMsg msg) {
        msgThreadLocal.set(msg);
    }

    public static RpcMsg getMsg() {
        return msgThreadLocal.get();
    }

    /**
     * 清理所有
     */
    public static void clear() {
        all.forEach(ThreadLocal::remove);
    }

    static class ThreadLocalUtil<T> {
        private ThreadLocal<T> local = new ThreadLocal<>();

        public void setLocal(T t) {
            local.set(t);
        }

        public T get() {
            return local.get();
        }

        public void remove() {
            local.remove();
        }
    }
}
