package com.tt.core.message.java.codec.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * list类型
 */
@SuppressWarnings("rawtypes")
public class ListCodec extends BaseCollectionCodec<List> {

    @Override
    protected List createNewInstance(int length) {
        return new ArrayList(length);
    }

    @Override
    protected List getEmptyInstance() {
        return Collections.emptyList();
    }
}
