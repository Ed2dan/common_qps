package com.paxar.qps.common.dao;

import com.paxar.qps.common.dao.statementutils.SqlParameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bandr on 31.08.2016.
 */
public class PreparedStatementArgumentSetter {

    private static List<SqlParameter> wrapArguments(List<Object> args) {
        List<SqlParameter> result = new ArrayList<>();
        for (Object arg : args) {
            result.add(SqlParameter.wrapArgument(arg));
        }
        return result;
    }

    public static void passArguments(PreparedStatement statement, List<Object> args) throws DatabaseException {
        List<SqlParameter> sqlParameters = wrapArguments(args);
        try {
            for (int i = 0; i < args.size(); i++) {
                SqlParameter parameter = sqlParameters.get(i);
                parameter.passArgument(statement, i + 1);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
