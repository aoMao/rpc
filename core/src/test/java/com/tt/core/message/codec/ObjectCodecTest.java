package com.tt.core.message.codec;

import com.tt.core.message.java.codec.base.IntegerCodec;
import com.tt.core.message.java.codec.JavaCodecUtil;
import com.tt.core.message.java.codec.base.LongCodec;
import com.tt.core.message.java.codec.testMessage.CodecTestBean;
import com.tt.core.message.java.codec.testMessage.InnerTest;
import com.tt.core.message.java.codec.testMessage.ObjectTestCodec;
import com.tt.core.message.java.codec.testMessage.ObjectTestCodec2;
import com.tt.message.constant.ServerType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ObjectCodecTest {

    //    @Test
    public void testLongCodec() {
        LongCodec codec = new LongCodec();
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; ++i) {
            byte[] data = codec.encode((long) i);
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
            buf.writeBytes(data);
            long codecI = codec.decode(buf);
            assert codecI == i : String.format("i=%d, codecI=%d", i, codecI);
            buf.release();
        }
    }

    //    @Test
    public void testIntegerCodec() {
        IntegerCodec codec = new IntegerCodec();
        for (int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; ++i) {
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
            codec.encode(i, buf);
            int codecI = codec.decode(buf);
            assert codecI == i : String.format("i=%d, codecI=%d", i, codecI);
            buf.release();
        }
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();

        int a = Integer.MAX_VALUE;
        System.out.println(a);
        codec.encode(a, buf);
        System.out.println(codec.decode(buf));
        buf.writeByte(100);
        buf.writeByte(11);
        System.out.println(buf.readByte());
        System.out.println(buf.readByte());
    }


    @Test
    public void testCodec() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        CodecTestBean bean = new CodecTestBean();
        bean.list = new ArrayList<>();
        bean.list.add(1000);
        bean.a = 100;
        bean.l = 10;
        bean.str = "asdf";
        bean.arr = new InnerTest[0];
        bean.map = new HashMap<>();
        List<Integer> list = new ArrayList<>();
        list.add(1230);
        bean.map.put("asdfa", list);
        bean.set = new HashSet<>();
        bean.set.add(1231);
        JavaCodecUtil.writeValueAndRegisterCodec(buf, bean, bean.getClass());
        CodecTestBean decode = JavaCodecUtil.readValueAndRegisterCodec(buf, bean.getClass());
        assert bean.equals(decode);
    }

    @Test
    public void testObjectCodec() {
        // obj2为obj多了一个字段的升级版本
        // write read
        System.out.println(JavaCodecUtil.getOrRegisterCodec(int[].class));
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        ObjectTestCodec objectTestCodec = generateObjectTextCodec();
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec, objectTestCodec.getClass());
        ObjectTestCodec objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        check(objectTestCodec, objectTestCodec1);
        buf.release();

        // write null
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        JavaCodecUtil.writeValueAndRegisterCodec(buf, null, objectTestCodec.getClass());
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        assert objectTestCodec1 == null;

        // encode read
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        byte[] data = JavaCodecUtil.encodeValueAndRegisterCodec(objectTestCodec);
        buf.writeBytes(data);
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        check(objectTestCodec, objectTestCodec1);
        buf.release();

        // encode null
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        data = JavaCodecUtil.encodeValueAndRegisterCodec(null);
        buf.writeBytes(data);
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        assert objectTestCodec1 == null;

        // write obj2 read obj
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        ObjectTestCodec2 objectTestCodec2 = generateObjectTextCodec2();
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec2, objectTestCodec2.getClass());
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        check(objectTestCodec2, objectTestCodec1);
        buf.release();

        // write obj2 write obj read obj2 read obj
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec2, objectTestCodec2.getClass());
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec, objectTestCodec.getClass());
        ObjectTestCodec2 objectTestCodec21 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec2.getClass());
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        check(objectTestCodec2, objectTestCodec21);
        check(objectTestCodec, objectTestCodec1);
        buf.release();

        // write obj2 write obj read obj read obj
        buf = PooledByteBufAllocator.DEFAULT.buffer();
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec2, objectTestCodec2.getClass());
        JavaCodecUtil.writeValueAndRegisterCodec(buf, objectTestCodec, objectTestCodec.getClass());
        ObjectTestCodec objectTestCodec3 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        objectTestCodec1 = JavaCodecUtil.readValueAndRegisterCodec(buf, objectTestCodec.getClass());
        check(objectTestCodec2, objectTestCodec3);
        check(objectTestCodec, objectTestCodec1);
        buf.release();
    }

    private ObjectTestCodec generateObjectTextCodec() {
        ObjectTestCodec objectTestCodec = new ObjectTestCodec();
        objectTestCodec.bo = true;
        objectTestCodec.f = 1f;
        objectTestCodec.i = 23;
        objectTestCodec.l = Long.MAX_VALUE - 1;
        objectTestCodec.d = 34.65d;
        objectTestCodec.b = 13;
        objectTestCodec.c = '2';
        objectTestCodec.s = 1235;
        objectTestCodec.str = "asdfqwer";
        objectTestCodec.arrInt = new int[10];
        objectTestCodec.serverType = ServerType.AUTH;
        for (int i = 0; i < 10; ++i) {
            objectTestCodec.arrInt[i] = i;
        }
        objectTestCodec.arrInner = new InnerTest[10];
        for (int i = 0; i < 10; ++i) {
            objectTestCodec.arrInner[i] = new InnerTest();
            objectTestCodec.arrInner[i].a = i;
        }
        InnerTest innerTest = new InnerTest();
        innerTest.a = 2513;
        objectTestCodec.innerTest = innerTest;
        return objectTestCodec;
    }

    private ObjectTestCodec2 generateObjectTextCodec2() {
        ObjectTestCodec2 objectTestCodec = new ObjectTestCodec2();
        objectTestCodec.bo = true;
        objectTestCodec.f = 1f;
        objectTestCodec.i = 23;
        objectTestCodec.l = Long.MAX_VALUE - 1;
        objectTestCodec.d = 34.65d;
        objectTestCodec.b = 13;
        objectTestCodec.c = '2';
        objectTestCodec.s = 1235;
        objectTestCodec.str = "asdfqwer";
        objectTestCodec.arrInt = new int[10];
        for (int i = 0; i < 10; ++i) {
            objectTestCodec.arrInt[i] = i;
        }
        objectTestCodec.arrInner = new InnerTest[10];
        for (int i = 0; i < 10; ++i) {
            objectTestCodec.arrInner[i] = new InnerTest();
            objectTestCodec.arrInner[i].a = i;
        }
        InnerTest innerTest = new InnerTest();
        innerTest.a = 2513;
        objectTestCodec.innerTest = innerTest;
        return objectTestCodec;
    }

    private void check(ObjectTestCodec objectTestCodec, ObjectTestCodec objectTestCodec1) {
        assert objectTestCodec1.innerTest.a == objectTestCodec.innerTest.a;
        assert objectTestCodec1.b == objectTestCodec.b;
        assert objectTestCodec1.i == objectTestCodec.i;
        assert objectTestCodec1.bo == objectTestCodec.bo;
        assert objectTestCodec1.f == objectTestCodec.f;
        assert objectTestCodec1.d == objectTestCodec.d;
        assert objectTestCodec1.c == objectTestCodec.c;
        assert objectTestCodec1.s == objectTestCodec.s;
        assert objectTestCodec1.l == objectTestCodec.l;
        assert objectTestCodec.str.equals(objectTestCodec1.str);
//        assert objectTestCodec1.serverType == objectTestCodec.serverType;
        for (int i = 0; i < 10; ++i) {
            assert objectTestCodec.arrInt[i] == objectTestCodec1.arrInt[i];
            assert objectTestCodec.arrInner[i].a == objectTestCodec1.arrInner[i].a;
        }
    }

    private void check(ObjectTestCodec2 objectTestCodec, ObjectTestCodec objectTestCodec1) {
        assert objectTestCodec1.innerTest.a == objectTestCodec.innerTest.a;
        assert objectTestCodec1.b == objectTestCodec.b;
        assert objectTestCodec1.i == objectTestCodec.i;
        assert objectTestCodec1.bo == objectTestCodec.bo;
        assert objectTestCodec1.f == objectTestCodec.f;
        assert objectTestCodec1.d == objectTestCodec.d;
        assert objectTestCodec1.c == objectTestCodec.c;
        assert objectTestCodec1.s == objectTestCodec.s;
        assert objectTestCodec1.l == objectTestCodec.l;
        assert objectTestCodec.str.equals(objectTestCodec1.str);
        for (int i = 0; i < 10; ++i) {
            assert objectTestCodec.arrInt[i] == objectTestCodec1.arrInt[i];
            assert objectTestCodec.arrInner[i].a == objectTestCodec1.arrInner[i].a;
        }
    }

    private void check(ObjectTestCodec2 objectTestCodec, ObjectTestCodec2 objectTestCodec1) {
        assert objectTestCodec1.innerTest.a == objectTestCodec.innerTest.a;
        assert objectTestCodec1.b == objectTestCodec.b;
        assert objectTestCodec1.i == objectTestCodec.i;
        assert objectTestCodec1.bo == objectTestCodec.bo;
        assert objectTestCodec1.f == objectTestCodec.f;
        assert objectTestCodec1.d == objectTestCodec.d;
        assert objectTestCodec1.c == objectTestCodec.c;
        assert objectTestCodec1.s == objectTestCodec.s;
        assert objectTestCodec1.l == objectTestCodec.l;
        assert objectTestCodec.str.equals(objectTestCodec1.str);
        for (int i = 0; i < 10; ++i) {
            assert objectTestCodec.arrInt[i] == objectTestCodec1.arrInt[i];
            assert objectTestCodec.arrInner[i].a == objectTestCodec1.arrInner[i].a;
        }
    }
}
