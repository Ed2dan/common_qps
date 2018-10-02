package com.paxar.qps.common.utils.stream.function;

@FunctionalInterface
public interface Function3<R, T, K, L> {

    R apply(T t, K k, L l) throws Exception;
}
