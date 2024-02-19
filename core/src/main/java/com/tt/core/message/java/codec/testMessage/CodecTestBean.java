package com.tt.core.message.java.codec.testMessage;

import com.tt.anno.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode
public class CodecTestBean {
    @Tag(1)
    public int a;
    @Tag(2)
    public long l;
    @Tag(3)
    public String str;
    @Tag(4)
    public InnerTest[] arr;
    @Tag(5)
    public List<Integer> list;
    @Tag(6)
    public Map<String, List<Integer>> map;
    public Set<Integer> set;
}
