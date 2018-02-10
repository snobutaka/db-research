package com.github.snobutaka.dbresearch.generatedkeys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSqlDbAccess implements DbAccess {

    public Connection getConnection(String host, int port)
            throws ClassNotFoundException, SQLException {

        Class.forName("org.postgresql.Driver");
        StringBuilder url = new StringBuilder();
        url.append("jdbc:postgresql://").append(host).append(":").append(port).append("/postgres");
        return DriverManager.getConnection(url.toString(), "postgres", "");
    }

    public void createTable(Connection conn) throws SQLException {
        conn.prepareStatement("CREATE SEQUENCE test_seq START 1").executeUpdate();

        StringBuilder createTable = new StringBuilder();
        createTable.append("CREATE TABLE test_table (");
        createTable.append("  id     BIGINT PRIMARY KEY DEFAULT nextval('test_seq'),");
        createTable.append("  value  TEXT");
        createTable.append(");");
        conn.prepareStatement(createTable.toString()).executeUpdate();
    }

    @Override
    public Data getDataFromGeneratedKeys(ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            return new Data(generatedKeys.getLong("id"), generatedKeys.getString("value"));
        } else {
            return null;
        }
    }
}
