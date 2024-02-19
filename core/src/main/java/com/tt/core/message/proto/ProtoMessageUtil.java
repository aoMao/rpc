package com.tt.core.message.proto;

import com.google.protobuf.Message;
import com.google.protobuf.Parser;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ProtoMessageUtil {
    static class Inner {
        private static final ProtoMessageUtil instance = new ProtoMessageUtil();
    }

    private ProtoMessageUtil() {
        init();
    }

    public static ProtoMessageUtil getInstance() {
        return Inner.instance;
    }

    private static final Map<Type, Parser<? extends Message>> clzToParseMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    private void init() {
        Reflections reflections = new Reflections("com.tt.message.proto");
        var clzSet = reflections.getSubTypesOf(Message.class);
        for (Class<? extends Message> aClass : clzSet) {
            if (aClass.isInterface() || !Message.class.isAssignableFrom(aClass)) {
                continue;
            }
            var modifier = aClass.getModifiers();
            if (Modifier.isAbstract(modifier)) {
                continue;
            }
            try {
                Field field = aClass.getDeclaredField("PARSER");
                field.setAccessible(true);
                Parser<? extends Message> parse = (Parser<? extends Message>) field.get(null);
                clzToParseMap.put(aClass, parse);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Parser<? extends Message> getParseByClz(Type clz) {
        return clzToParseMap.get(clz);
    }
}
