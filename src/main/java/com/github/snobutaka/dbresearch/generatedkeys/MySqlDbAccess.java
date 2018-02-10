package com.github.snobutaka.dbresearch.generatedkeys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlDbAccess implements DbAccess {

    public Connection getConnection(String host, int port)
            throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.jdbc.Driver");
        StringBuilder url = new StringBuilder();
        url.append("jdbc:mysql://").append(host).append(":").append(port).append("/test_db");
        return DriverManager.getConnection(url.toString(), "root", "root");
    }

    @Override
    public void createTable(Connection conn) throws SQLException {
        StringBuilder createTable = new StringBuilder();
        createTable.append("CREATE TABLE test_db.test_table (");
        createTable.append("  id BIGINT AUTO_INCREMENT,");
        createTable.append("  value TEXT,");
        createTable.append("  INDEX(id)");
        createTable.append(");");

        conn.prepareStatement(createTable.toString()).executeUpdate();
    }

    @Override
    public Data getDataFromGeneratedKeys(ResultSet generatedKeys) throws SQLException {
        if (generatedKeys.next()) {
            long id = generatedKeys.getLong(1);
            // long id = generatedKeys.getLong("id"); // Error
            String value = null;
            try {
                value = generatedKeys.getString(2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return new Data(id, value);
        } else {
            return null;
        }
    }
}
