package com.tt.core.net.util;

import com.tt.core.constant.MessageConst;
import com.tt.core.message.MessageManager;
import com.tt.message.anno.Message;
import org.reflections.Reflections;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MessageUtil {

    private static final Reflections r = new Reflections(MessageConst.MESSAGE_BASE_PACKAGE_NAME);
    private static final Map<String, Set<Class<?>>> packageToMsgClzSetMap = new ConcurrentHashMap<>();

    public static Set<Class<?>> getMessageClzSet(String packageName) {
        return packageToMsgClzSetMap.computeIfAbsent(packageName, k -> {
            Reflections reflections = new Reflections(packageName);
            return reflections.getTypesAnnotatedWith(Message.class);
        });
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Set<Class<?>> getMessageClzSet(Class clz) {
        return r.getSubTypesOf(clz);
    }

    /**
     * 注册消息
     *
     * @param messageManager
     * @param superClz
     * @param instanceList
     * @throws Exception
     */
    public static <T> void registerMsg(MessageManager messageManager, Class<?> superClz, List<T> instanceList) throws
            Exception {
        var clzSet = getMessageClzSet(superClz);
        registerMsg(messageManager, clzSet, instanceList);
    }

    /**
     * 注册消息
     *
     * @param messageManager
     * @param clzSet
     * @param instanceList
     * @throws Exception
     */
    public static <T> void registerMsg(MessageManager messageManager, Set<Class<?>> clzSet, List<T> instanceList) throws
            Exception {
        var list = instanceList == null ? Collections.emptyList() : instanceList.stream()
                .filter(instance -> !(instance instanceof Proxy))
                .collect(Collectors.toList());
        for (Class<?> clz : clzSet) {
            if (checkHasInstance(clz, list)) {
                continue;
            }
            messageManager.registerMsg(clz);
        }
        // 注册需要处理的消息，上面可能已经注册过了，这里会直接覆盖
        for (Object instance : list) {
            messageManager.registerMsg(instance);
        }
    }

    public static <T> boolean checkHasInstance(Class<?> clz, List<T> instanceList) {
        if (instanceList == null || instanceList.isEmpty()) {
            return false;
        }
        return instanceList.stream()
                .anyMatch(clz::isInstance);
    }
}
