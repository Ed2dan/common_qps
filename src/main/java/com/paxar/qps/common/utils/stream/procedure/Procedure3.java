package com.paxar.qps.common.utils.stream.procedure;

@FunctionalInterface
public interface Procedure3<R, T, K> {

    void accept(R r, T t, K k) throws Exception;
}
