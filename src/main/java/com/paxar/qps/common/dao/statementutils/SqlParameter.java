package com.paxar.qps.common.dao.statementutils;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bandr on 31.08.2016.
 */
public abstract class SqlParameter<T> {

    protected final T value;

    public SqlParameter(T value) {
        this.value = value;
    }

    public abstract void passArgument(PreparedStatement ps, int position) throws SQLException;

    public static SqlParameter wrapArgument(Object o) {
        SupportedTypes type = SupportedTypes.getValue(o.getClass());
        if (type == null) {
            throw new RuntimeException("Invalid argument type");
        }
        switch (type){
            case STRING:
                return new StringParameter((String) o);
            case BOOLEAN:
                return new BooleanParameter((Boolean) o);
            case BYTE:
                return new ByteParameter((Byte) o);
            case DATE:
                return new DateParameter((Date) o);
            case DOUBLE:
                return new DoubleParameter((Double) o);
            case FLOAT:
                return new FloatParameter((Float) o);
            case INTEGER:
                return new IntegerParameter((Integer) o);
            case LONG:
                return new LongParameter((Long) o);
            case SHORT:
                return new ShortParameter((Short) o);
            case ARRAY:
                return new ArrayParameter(o);
            case BIG_DECIMAL:
                return new BigDecimalParameter((BigDecimal) o);
            default:
                throw new RuntimeException("Invalid argument type");
        }
    }
}
