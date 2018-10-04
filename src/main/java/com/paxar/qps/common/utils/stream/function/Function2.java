package com.paxar.qps.common.utils.stream.function;

/**
 * Like as {@link java.util.function.BiFunction}.
 */
@FunctionalInterface
public interface Function2<R, T, K> {

    R apply(T t, K k) throws Exception;
}
