package com.paxar.qps.common.utils.stream.procedure;

/**
 * Like as {@link java.util.function.Consumer}.
 */
@FunctionalInterface
public interface Procedure1<R> {

    void accept(R r) throws Exception;
}
