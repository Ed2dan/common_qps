package com.paxar.qps.common.utils.stream.procedure;

/**
 * Like as {@link java.util.function.BiConsumer}.
 */
@FunctionalInterface
public interface Procedure2<R, T> {

    void accept(R r, T t) throws Exception;
}
