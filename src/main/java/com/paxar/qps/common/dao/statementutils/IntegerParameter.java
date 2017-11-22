package com.paxar.qps.common.dao.statementutils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by bandr on 31.08.2016.
 */
class IntegerParameter extends SqlParameter<Integer> {

    IntegerParameter(Integer value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        ps.setInt(position, value);
    }
}
