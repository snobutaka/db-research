package com.github.snobutaka.dbresearch.generatedkeys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public interface DbAccess {

    public void createTable(Connection conn) throws SQLException;

    public Data getDataFromGeneratedKeys(ResultSet generatedKeys) throws SQLException;

    default Data insert(Connection conn, String  value) throws SQLException {
        String insert = "INSERT INTO test_table (value) VALUES (?)";
        PreparedStatement stmt = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, value);
        stmt.executeUpdate();

        return getDataFromGeneratedKeys(stmt.getGeneratedKeys());
    }

    default public Data update(Connection conn, long id, String value) throws SQLException {
        String update = "UPDATE test_table SET value = ? WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(update, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, value);
        stmt.setLong(2, id);
        stmt.executeUpdate();

        return getDataFromGeneratedKeys(stmt.getGeneratedKeys());
    }
}
