package com.tt.core.message.java.codec.testMessage;

import com.tt.anno.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class InnerTest {
    public int a;
    @Tag(7)
    public CodecTestBean bean;
}