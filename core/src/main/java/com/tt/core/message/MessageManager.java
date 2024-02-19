package com.tt.core.message;

import com.tt.anno.processor.MessageIdGenerateConstProcessor;
import com.tt.message.anno.Message;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
public class MessageManager {

    private final Map<Integer, MessageEntry> messageEntryMap = new ConcurrentHashMap<>();
    private final Map<String, MessageEntry> nameToEntryMap = new ConcurrentHashMap<>();
    private final Map<String, MessageEntry> nameToResultEntryMap = new ConcurrentHashMap<>();
    private final CopyOnWriteArraySet<Class<?>> allMsgClz = new CopyOnWriteArraySet<>();
    private Class<?>[] msgClzArray = new Class[0];

    /**
     * 消息注册
     *
     * @param interfaceClzSet
     */
    public void registerMsg(Set<Class<?>> interfaceClzSet) throws Exception {
        for (Class<?> clz : interfaceClzSet) {
            registerOneInterface(clz, null);
        }
    }

    /**
     * 消息注册
     *
     * @param interfaceClz
     */
    public void registerMsg(Class<?> interfaceClz) throws Exception {
        registerOneInterface(interfaceClz, null);
    }

    /**
     * 消息注册
     *
     * @param list
     */
    public void registerMsg(List<?> list) throws Exception {
        if (list == null) {
            return;
        }
        for (Object handler : list) {
            registerMsg(handler);
        }
    }

    /**
     * 消息注册
     *
     * @param instance
     */
    public void registerMsg(Object instance) throws Exception {
        for (Class<?> anInterface : instance.getClass().getInterfaces()) {
            registerOneInterface(anInterface, instance);
        }
    }

    private void registerOneInterface(Class<?> interfaceClz, Object instance) throws Exception {
        Message message = interfaceClz.getAnnotation(Message.class);
        if (message == null) {
            return;
        }
        allMsgClz.add(interfaceClz);
        msgClzArray = allMsgClz.toArray(new Class[0]);
        var idConstClz = getIdConstClass(interfaceClz);
        for (Method method : interfaceClz.getDeclaredMethods()) {
            int modifier = method.getModifiers();
            if (Modifier.isStatic(modifier) || Modifier.isPrivate(modifier) || !Modifier.isAbstract(modifier)) {
                continue;
            }
            if (Object.class.equals(method.getDeclaringClass())) {
                continue;
            }
            // 生成发送消息
            createEntry(method, idConstClz, instance, false);
            // 生成返回消息
            createEntry(method, idConstClz, instance, true);
        }
    }

    private void createEntry(Method method, Class<?> idConstClz, Object instance, boolean isResult) throws NoSuchFieldException, IllegalAccessException {
        int id = generateId(method, idConstClz, isResult);
        MessageEntry messageEntry = createMessageEntry(id, method, instance, isResult);
        String messageName = messageEntry.getMessageName();
        var oldEntry = isResult ? nameToResultEntryMap.get(messageName) : nameToEntryMap.get(messageName);
        if (oldEntry != null) {
            log.error("message name same, will replaced, name={}, oldMessageEntry={}, newMessageEntry={}",
                    messageName, oldEntry, messageEntry);
        }

        messageEntryMap.put(messageEntry.getId(), messageEntry);
        if (isResult) {
            nameToResultEntryMap.put(messageEntry.getMessageName(), messageEntry);
        } else {
            nameToEntryMap.put(messageEntry.getMessageName(), messageEntry);
        }
    }

    /**
     * 获取消息定义
     *
     * @param msgId
     * @return
     */
    public MessageEntry getMessageEntryById(int msgId) {
        return messageEntryMap.get(msgId);
    }

    /**
     * 获取消息定义
     *
     * @param msgName
     * @return
     */
    public MessageEntry getMessageEntryByName(String msgName) {
        return nameToEntryMap.get(msgName);
    }

    /**
     * 获取消息定义
     *
     * @param msgName
     * @return
     */
    public MessageEntry getResultMessageEntryByName(String msgName) {
        return nameToResultEntryMap.get(msgName);
    }

    /**
     * 创建消息结构
     *
     * @param id
     * @param method
     * @param instance
     * @return
     */
    protected MessageEntry createMessageEntry(int id, Method method, Object instance, boolean isResult) {
        return new MessageEntry(id, method, instance, isResult);
    }

    /**
     * 生成消息id
     *
     * @param method
     * @param idConstClz
     * @return
     */
    protected int generateId(Method method, Class<?> idConstClz, boolean isResult) throws NoSuchFieldException, IllegalAccessException {
        if (isResult) {
            return getMsgResultId(idConstClz, method);
        } else {
            return getMsgId(idConstClz, method);
        }
    }

    private Class<?> getIdConstClass(Class<?> interfaceClz) throws ClassNotFoundException {
        return Class.forName(
                String.format("%s.constant.%sIdConst", interfaceClz.getPackageName(), interfaceClz.getSimpleName()));
    }

    private int getMsgId(Class<?> idConstClz, Method method) throws NoSuchFieldException, IllegalAccessException {
        var fieldName = MessageIdGenerateConstProcessor.parseMethodName(method.getName());
        var field = idConstClz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (int) field.get(null);
    }

    private int getMsgResultId(Class<?> idConstClz, Method method) throws NoSuchFieldException, IllegalAccessException {
        var fieldName = MessageIdGenerateConstProcessor.parseMethodName(method.getName()) + "_RESULT";
        var field = idConstClz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (int) field.get(null);
    }

    public Class<?>[] getAllMsgClz() {
        return msgClzArray;
    }
}
