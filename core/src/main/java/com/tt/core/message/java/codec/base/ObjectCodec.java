package com.tt.core.message.java.codec.base;

import com.tt.anno.Tag;
import com.tt.core.message.java.codec.BaseReferenceCodec;
import com.tt.core.message.java.codec.IJavaTypeCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import com.tt.core.message.java.codec.exception.CodecException;
import io.netty.buffer.ByteBuf;

import java.beans.Transient;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 对象属性解析
 */
public class ObjectCodec extends BaseReferenceCodec<Object> {

    private Constructor<?> noParamConstructor;
    private Field[] fields;
    @SuppressWarnings("rawtypes")
    private volatile IJavaTypeCodec[] codecCaches;

    private static final Object NULL = null;

    @Override
    @SuppressWarnings({"rawtypes", "ConstantConditions"})
    public Object decode(ByteBuf buf) {
        int length = JavaCodecUtil.readValue(buf, int.class);
        if (length == 0) {
            return NULL;
        }
        int beginReaderIndex = buf.readerIndex();
        try {
            Object instance = noParamConstructor.newInstance();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                IJavaTypeCodec codec = codecCaches[i];
                Object value = codec.decode(buf);
                field.set(instance, value);
                if (buf.readerIndex() - beginReaderIndex >= length) {
                    break;
                }
            }
            return instance;
        } catch (Exception e) {
            throw new CodecException(e);
        } finally {
            int needReadLength = length - (buf.readerIndex() - beginReaderIndex);
            if (needReadLength > 0) {
                buf.skipBytes(needReadLength);
            }
        }
    }

    @Override
    public int encode(Object value, ByteBuf buf) {
        var data = encode(value);
        buf.writeBytes(data);
        return data.length;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public byte[] encode(Object value) {
        if (value == null) {
            return JavaCodecUtil.INT_0_BYTES;
        }

        List<byte[]> bytesList = new ArrayList<>(fields.length);
        try {
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                IJavaTypeCodec codec = codecCaches[i];
                var fieldValue = field.get(value);
                bytesList.add(codec.encode(fieldValue));
            }
        } catch (IllegalAccessException e) {
            throw new CodecException(e);
        }
        return JavaCodecUtil.bytesListToByteArray(bytesList);
    }

    @Override
    protected void init(Type type, Class<?> clz, Set<Type> parents) {
        try {
            noParamConstructor = clz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new CodecException(e);
        }
        checkFields(clz);
        // 检查是否有循环引用
        checkReferenceLoop(type, parents);
        // 将属性排序，防止出现由于读取field顺序不一致造成的序列化错误
        Arrays.sort(fields, (o1, o2) -> {
            Tag tag1 = o1.getAnnotation(Tag.class);
            Tag tag2 = o2.getAnnotation(Tag.class);
            if (tag1 == null && tag2 == null) {
                return o1.getName().compareTo(o2.getName());
            } else if (tag1 == null) {
                return 1;
            } else if (tag2 == null) {
                return -1;
            }
            return tag1.value() - tag2.value();
        });
        // 初始化field的codec前，需要先将当前类型加进去，以便进行循环引用检查
        parents.add(type);
        codecCaches = new IJavaTypeCodec[fields.length];
        for (int j = 0; j < fields.length; j++) {
            codecCaches[j] = JavaCodecUtil.getOrRegisterCodec(fields[j].getGenericType(), parents);
        }
        // 初始化之后需要删除，防止多个相同类型的field检测失败
        parents.remove(type);
    }

    /**
     * 获取所有的有用的field
     *
     * @param clz
     */
    private void checkFields(Class<?> clz) {
        Field[] allFields = clz.getDeclaredFields();
        int length = allFields.length;
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) ||
                    field.isAnnotationPresent(Transient.class)) {
                --length;
            }
        }
        fields = new Field[length];
        int i = 0;
        for (Field field : allFields) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) ||
                    field.isAnnotationPresent(Transient.class)) {
                continue;
            }
            fields[i++] = field;
            field.setAccessible(true);
        }
    }

}
