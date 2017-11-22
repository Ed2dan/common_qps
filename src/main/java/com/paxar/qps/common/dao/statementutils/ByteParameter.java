package com.paxar.qps.common.dao.statementutils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by bandr on 31.08.2016.
 */
class ByteParameter extends SqlParameter<Byte> {

    ByteParameter(Byte value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        ps.setByte(position, value);
    }
}
