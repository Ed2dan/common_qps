package com.paxar.qps.common.dao.statementutils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by bandr on 31.08.2016.
 */
class BooleanParameter extends SqlParameter<Boolean> {

    BooleanParameter(Boolean value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        ps.setBoolean(position, value);
    }
}
