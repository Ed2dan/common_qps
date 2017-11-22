package com.paxar.qps.common.dao.statementutils;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by bandr on 31.08.2016.
 */
class StringParameter extends SqlParameter<String> {


    StringParameter(String value) {
        super(value);
    }

    @Override
    public void passArgument(PreparedStatement ps, int position) throws SQLException {
        ps.setString(position, value);
    }

}
