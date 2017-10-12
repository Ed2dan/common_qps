package com.paxar.qps.common.dao.statementutils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bandr on 31.08.2016.
 */
enum SupportedTypes {
    BIG_DECIMAL, BOOLEAN, BYTE, DATE, DOUBLE, FLOAT, INTEGER, LONG, SHORT, STRING, ARRAY;

    private static final Map<Class, SupportedTypes> TYPES_MAP = new HashMap<Class, SupportedTypes>() {{
        put(BigDecimal.class, SupportedTypes.BIG_DECIMAL);
        put(Boolean.class, SupportedTypes.BOOLEAN);
        put(Byte.class, SupportedTypes.BYTE);
        put(Date.class, SupportedTypes.DATE);
        put(Double.class, SupportedTypes.DOUBLE);
        put(Float.class, SupportedTypes.FLOAT);
        put(Integer.class, SupportedTypes.INTEGER);
        put(Long.class, SupportedTypes.LONG);
        put(Short.class, SupportedTypes.SHORT);
        put(boolean.class, SupportedTypes.BOOLEAN);
        put(byte.class, SupportedTypes.BYTE);
        put(double.class, SupportedTypes.DOUBLE);
        put(float.class, SupportedTypes.FLOAT);
        put(int.class, SupportedTypes.INTEGER);
        put(long.class, SupportedTypes.LONG);
        put(short.class, SupportedTypes.SHORT);
        put(String.class, SupportedTypes.STRING);
    }};
    private static final Map<SupportedTypes, String> TYPE_TO_POSTGRES = new HashMap<SupportedTypes, String>() {{
        put(BIG_DECIMAL, "numeric");
        put(BOOLEAN, "boolean");
        put(BYTE, "int");
        put(DATE, "timestamp");
        put(DOUBLE, "float8");
        put(FLOAT, "real");
        put(INTEGER, "int");
        put(LONG, "bigint");
        put(SHORT, "int2");
        put(STRING, "text");
    }};


    public static SupportedTypes getValue(Class clazz) {
        if (clazz.isArray()) {
            return ARRAY;
        }
        return TYPES_MAP.get(clazz);
    }

    public String getSqlName() {
        return TYPE_TO_POSTGRES.get(this);
    }
}
