package com.paxar.qps.common.dao.statementutils;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by bandr on 31.08.2016.
 */
class ArrayParameter extends SqlParameter<Object> {
    ArrayParameter(Object value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        Connection connection = ps.getConnection();
        Class componentType = value.getClass().getComponentType();
        Array sqlArray = null;
        if (componentType.isPrimitive()) {
            Object[] valueArray = null;
            if (componentType.equals(int.class)) {
                valueArray = Arrays.asList((int[]) value).toArray();
            } else if (componentType.equals(boolean.class)) {
                valueArray = Arrays.asList((boolean[]) value).toArray();
            } else if (componentType.equals(byte.class)) {
                valueArray = Arrays.asList((byte[]) value).toArray();
            } else if (componentType.equals(double.class)) {
                valueArray = Arrays.asList((double[]) value).toArray();
            } else if (componentType.equals(float.class)) {
                valueArray = Arrays.asList((float[]) value).toArray();
            } else if (componentType.equals(long.class)) {
                valueArray = Arrays.asList((long[]) value).toArray();
            } else if (componentType.equals(short.class)) {
                valueArray = Arrays.asList((short[]) value).toArray();
            }
            sqlArray = connection.createArrayOf(SupportedTypes.getValue(componentType).getSqlName(), valueArray);
        } else {
            sqlArray = connection.createArrayOf(SupportedTypes.getValue(componentType).getSqlName(), (Object[]) value);
        }
        ps.setArray(position, sqlArray);
    }
}
