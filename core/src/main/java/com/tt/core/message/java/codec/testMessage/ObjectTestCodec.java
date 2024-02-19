package com.tt.core.message.java.codec.testMessage;

import com.tt.anno.Tag;
import com.tt.message.constant.ServerType;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ObjectTestCodec {
    @Tag(1)
    public int i;
    @Tag(2)
    public long l;
    @Tag(3)
    public float f;
    @Tag(4)
    public double d;
    @Tag(5)
    public byte b;
    @Tag(6)
    public char c;
    @Tag(7)
    public String str;
    @Tag(8)
    public short s;
    @Tag(9)
    public boolean bo;
    @Tag(10)
    public InnerTest innerTest;
    @Tag(11)
    public int[] arrInt;
    @Tag(12)
    public InnerTest[] arrInner;

    public ServerType serverType;

    static int a = 1000;
}