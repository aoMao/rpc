package com.tt.core.message.java.codec;

import com.tt.core.message.java.codec.base.*;
import com.tt.core.message.java.codec.factory.*;
import com.tt.core.util.ReflectionUtil;
import io.netty.buffer.ByteBuf;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * java编解码工具，保存编解码类的唯一性
 */
@SuppressWarnings("rawtypes")
public class JavaCodecUtil {
    /**
     * 基本类型编解码器映射
     */
    private static final Map<Type, IJavaTypeCodec> baseCodecMap = new HashMap<>();

    /**
     * 使用ConcurrentHashMap，会有多线程修改
     */
    private static final Map<Type, IJavaTypeCodec> objectCodecMap = new ConcurrentHashMap<>();

    private static final List<IReferenceCodecFactory> codecFactories = new ArrayList<>();

    public static final byte[] TRUE_BYTES;
    public static final byte[] FALSE_BYTES;

    public static final byte[] INT_0_BYTES;

    static {
        // int
        IntegerCodec integerCodec = new IntegerCodec();
        baseCodecMap.put(int.class, integerCodec);
        baseCodecMap.put(Integer.class, integerCodec);
        // long
        LongCodec longCodec = new LongCodec();
        baseCodecMap.put(long.class, longCodec);
        baseCodecMap.put(Long.class, longCodec);
        // double
        DoubleCodec doubleCodec = new DoubleCodec();
        baseCodecMap.put(double.class, doubleCodec);
        baseCodecMap.put(Double.class, doubleCodec);
        // float
        FloatCodec floatCodec = new FloatCodec();
        baseCodecMap.put(float.class, floatCodec);
        baseCodecMap.put(Float.class, floatCodec);
        // short
        ShortCodec shortCodec = new ShortCodec();
        baseCodecMap.put(short.class, shortCodec);
        baseCodecMap.put(Short.class, shortCodec);
        // char
        CharCodec charCodec = new CharCodec();
        baseCodecMap.put(char.class, charCodec);
        baseCodecMap.put(Character.class, charCodec);
        // boolean
        BooleanCodec booleanCodec = new BooleanCodec();
        baseCodecMap.put(boolean.class, booleanCodec);
        baseCodecMap.put(Boolean.class, booleanCodec);
        // byte
        ByteCodec byteCodec = new ByteCodec();
        baseCodecMap.put(byte.class, byteCodec);
        baseCodecMap.put(Byte.class, byteCodec);
        // string
        baseCodecMap.put(String.class, new StringCodec());
        TRUE_BYTES = encodeValue(true);
        FALSE_BYTES = encodeValue(false);
        INT_0_BYTES = encodeValue(0);

        codecFactories.add(new EnumCodecFactory());
        codecFactories.add(new ArrayCodecFactory());
        codecFactories.add(new SetCodecFactory());
        codecFactories.add(new ListCodecFactory());
        codecFactories.add(new MapCodecFactory());
    }

    /**
     * 读取基础类型
     *
     * @param buf
     * @param clz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T readValue(ByteBuf buf, Class<T> clz) {
        IJavaTypeCodec<T> codec = baseCodecMap.get(clz);
        if (codec == null) {
            return null;
        }
        return codec.decode(buf);
    }


    /**
     * 写基础类型
     *
     * @param value
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] encodeValue(T value) {
        IJavaTypeCodec<T> codec = baseCodecMap.get(value.getClass());
        if (codec == null) {
            return new byte[0];
        }
        return codec.encode(value);
    }

    /**
     * 写基础类型
     *
     * @param buf
     * @param value
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> int writeValue(ByteBuf buf, T value) {
        IJavaTypeCodec<T> codec = baseCodecMap.get(value.getClass());
        if (codec == null) {
            return 0;
        }
        return codec.encode(value, buf);
    }

    /**
     * 注册类对象编解码
     *
     * @param type
     * @param parents
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static IJavaTypeCodec registerObjectCodec(Type type, Set<Type> parents) {
        BaseReferenceCodec codec = null;
        Class<?> clz = ReflectionUtil.typeToClass(type);
        for (IReferenceCodecFactory codecFactory : codecFactories) {
            if (codecFactory.check(type, clz)) {
                codec = codecFactory.create(type, clz);
                break;
            }
        }
        if (codec == null) {
            codec = new ObjectCodec();
        }
        codec.init(type, clz, parents);
        return codec;
    }

    /**
     * 读取基础类型
     *
     * @param buf
     * @param type
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T readValueAndRegisterCodec(ByteBuf buf, Type type) {
        IJavaTypeCodec<T> codec = (IJavaTypeCodec<T>) getOrRegisterCodec(type, new HashSet<>());
        return codec.decode(buf);
    }


    /**
     * 写基础类型
     *
     * @param value
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] encodeValueAndRegisterCodec(T value) {
        if (value == null) {
            return FALSE_BYTES;
        }
        IJavaTypeCodec<T> codec = (IJavaTypeCodec<T>) getOrRegisterCodec(value.getClass());
        return codec.encode(value);
    }

    /**
     * 写基础类型
     *
     * @param value
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> byte[] encodeValueAndRegisterCodec(T value, Type type) {
        if (value == null) {
            return FALSE_BYTES;
        }
        IJavaTypeCodec<T> codec = (IJavaTypeCodec<T>) getOrRegisterCodec(type);
        return codec.encode(value);
    }

    /**
     * 写基础类型
     *
     * @param buf
     * @param value
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    public static <T> int writeValueAndRegisterCodec(ByteBuf buf, T value, Type type) {
        if (value == null) {
            buf.writeBytes(INT_0_BYTES);
            return INT_0_BYTES.length;
        }
        IJavaTypeCodec<T> codec = (IJavaTypeCodec<T>) getOrRegisterCodec(type);
        return codec.encode(value, buf);
    }

    /**
     * 获取类的编解码器
     *
     * @param type
     * @return
     */
    public static IJavaTypeCodec<?> getOrRegisterCodec(Type type) {
        IJavaTypeCodec<?> codec = baseCodecMap.get(type);
        if (codec == null) {
            codec = objectCodecMap.get(type);
            if (codec == null) {
                IJavaTypeCodec finalCodec = registerObjectCodec(type, new HashSet<>());
                codec = objectCodecMap.computeIfAbsent(type, k -> finalCodec);
            }
        }
        return codec;
    }

    /**
     * 获取类的编解码器
     *
     * @param type
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static IJavaTypeCodec<?> getOrRegisterCodec(Type type, Set<Type> parents) {
        IJavaTypeCodec<?> codec = baseCodecMap.get(type);
        if (codec == null) {
            codec = objectCodecMap.get(type);
            if (codec == null) {
                IJavaTypeCodec finalCodec = registerObjectCodec(type, parents);
                codec = objectCodecMap.computeIfAbsent(type, k -> finalCodec);
            }
        }
        return codec;
    }

    /**
     * 将length编码，并加到数组最前方
     *
     * @param bytesList
     * @return
     */
    public static byte[] bytesListToByteArray(List<byte[]> bytesList) {
        int length = 0;
        for (byte[] bytes : bytesList) {
            length += bytes.length;
        }
        byte[] lengthData = encodeValue(length);
        return addLengthFront(bytesList, length, lengthData);
    }

    /**
     * 将length编码，并加到数组最前方
     *
     * @param length
     * @param bytesList
     * @return
     */
    public static byte[] bytesListToByteArray(int length, List<byte[]> bytesList) {
        byte[] lengthData = encodeValue(length);
        int dataLength = 0;
        for (byte[] bytes : bytesList) {
            dataLength += bytes.length;
        }
        return addLengthFront(bytesList, dataLength, lengthData);
    }

    /**
     * 将length放到最前方
     *
     * @param bytesList
     * @param length
     * @param lengthData
     * @return
     */
    private static byte[] addLengthFront(List<byte[]> bytesList, int length, byte[] lengthData) {
        byte[] result = new byte[length + lengthData.length];
        int i = 0;
        for (byte lengthDatum : lengthData) {
            result[i++] = lengthDatum;
        }
        for (byte[] bytes : bytesList) {
            for (byte b : bytes) {
                result[i++] = b;
            }
        }
        return result;
    }
}
