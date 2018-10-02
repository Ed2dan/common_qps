package com.paxar.qps.common.utils.stream;

import com.paxar.qps.common.utils.stream.function.Function0;
import com.paxar.qps.common.utils.stream.function.Function1;
import com.paxar.qps.common.utils.stream.function.Function2;
import com.paxar.qps.common.utils.stream.function.Function3;
import com.paxar.qps.common.utils.stream.procedure.Procedure0;
import com.paxar.qps.common.utils.stream.procedure.Procedure1;
import com.paxar.qps.common.utils.stream.procedure.Procedure2;
import com.paxar.qps.common.utils.stream.procedure.Procedure3;

/**
 * Lets you use checked exception in lambda expressions.
 */
public final class ThrowingLambdaUtils {

    private ThrowingLambdaUtils() {
    }

    public static <R> R wrap(Function0<R> function) {
        try {
            return function.apply();
        } catch (Exception ex) {
            return rethrow(ex);
        }
    }

    public static <R, T> R wrap(Function1<R, T> function, T t) {
        try {
            return function.apply(t);
        } catch (Exception ex) {
            return rethrow(ex);
        }
    }

    public static <R, T, K> R wrap(Function2<R, T, K> function, T t, K k) {
        try {
            return function.apply(t, k);
        } catch (Exception ex) {
            return rethrow(ex);
        }
    }

    public static <R, T, K, L> R wrap(Function3<R, T, K, L> function, T t, K k, L l) {
        try {
            return function.apply(t, k, l);
        } catch (Exception ex) {
            return rethrow(ex);
        }
    }

    public static void wrap(Procedure0 procedure) {
        try {
            procedure.accept();
        } catch (Exception ex) {
            rethrow(ex);
        }
    }


    public static <T> void wrap(Procedure1<T> procedure, T t) {
        try {
            procedure.accept(t);
        } catch (Exception ex) {
            rethrow(ex);
        }
    }

    public static <T, K> void wrap(Procedure2<T, K> procedure, T t, K k) {
        try {
            procedure.accept(t, k);
        } catch (Exception ex) {
            rethrow(ex);
        }
    }

    public static <T, K, L> void wrap(Procedure3<T, K, L> procedure, T t, K k, L l) {
        try {
            procedure.accept(t, k, l);
        } catch (Exception ex) {
            rethrow(ex);
        }
    }

    @SuppressWarnings("unchecked")
    private static <R, E extends Exception> R rethrow(Exception ex) throws E {
        throw (E) ex;
    }

}
