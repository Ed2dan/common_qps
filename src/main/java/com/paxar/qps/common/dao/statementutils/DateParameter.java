package com.paxar.qps.common.dao.statementutils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by bandr on 31.08.2016.
 */
class DateParameter extends SqlParameter<Date> {
    DateParameter(Date value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        ps.setTimestamp(position, new java.sql.Timestamp(value.getTime()));
    }
}
