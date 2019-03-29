package com.netease.message;

import java.util.IdentityHashMap;

class Context<T> {

    final T message;
    final IdentityHashMap<Object, Integer> cacheSerializedSize = new IdentityHashMap<>();

    Context(T message) {
        this.message = message;
    }


}
