package com.tt.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具
 */
public class ReflectionUtil {

    /**
     * 空type数组，享元模式
     */
    public static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    /**
     * type转class，如果是ParameterizedType，则返回对应的rawType
     *
     * @param type
     * @return
     */
    public static Class<?> typeToClass(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        } else {
            return (Class<?>) type;
        }
    }

    /**
     * 获取泛型实际对象
     *
     * @param type
     * @return
     */
    public static Type[] getActualTypeArguments(Type type) {
        if (type instanceof ParameterizedType parameterizedType) {
            return parameterizedType.getActualTypeArguments();
        }
        return EMPTY_TYPE_ARRAY;
    }

    /**
     * 获取方法或类上的注解
     *
     * @param method
     * @param annoClz
     * @param <T>
     * @return
     */
    public static <T extends Annotation> T getAnno(Method method, Class<T> annoClz) {
        var t = method.getAnnotation(annoClz);
        if (t == null) {
            var declaringClass = method.getDeclaringClass();
            t = declaringClass.getAnnotation(annoClz);
        }
        return t;
    }
}
