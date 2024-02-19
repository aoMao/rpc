package com.tt.core.message.java.codec.exception;

import java.lang.reflect.Type;

/**
 * 协议类型不支持编解码
 */
public class CodecNotSupportTypeException extends RuntimeException {

    public CodecNotSupportTypeException(Type type) {
        super("codec not support the type" + type.getTypeName());
    }
}
