package com.paxar.qps.common.utils.stream.function;

/**
 * Like as {@link java.util.function.Function}.
 * */
@FunctionalInterface
public interface Function1<R, T> {

    R apply(T t) throws Exception;
}
