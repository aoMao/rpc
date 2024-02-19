package com.tt.core.message.java.codec.collection;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * set类型
 */
@SuppressWarnings("rawtypes")
public class SetCodec extends BaseCollectionCodec<Set> {
    @Override
    protected Set createNewInstance(int length) {
        return new HashSet();
    }

    @Override
    protected Set getEmptyInstance() {
        return Collections.emptySet();
    }
}
