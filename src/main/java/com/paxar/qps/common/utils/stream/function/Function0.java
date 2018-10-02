package com.paxar.qps.common.utils.stream.function;

/**
 * Like as {@link java.util.concurrent.Callable}.
 */
@FunctionalInterface
public interface Function0<R> {

    R apply() throws Exception;

}
