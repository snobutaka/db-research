package com.github.snobutaka.dbresearch.generatedkeys;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.github.snobutaka.dbresearch.generatedkeys.Data;
import com.github.snobutaka.dbresearch.generatedkeys.MySqlDbAccess;

public class MySqlGeneratedKeyTest {
    static final int port = 3306;
    static Map<String, String> env = new HashMap<>();

    static {
        env.put("MYSQL_ROOT_PASSWORD", "root");
        env.put("MYSQL_DATABASE", "test_db");
    }

    @ClassRule
    public static GenericContainer mysql = new GenericContainer<>("mysql:5")
            .withExposedPorts(port).withEnv(env);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        MySqlDbAccess mysqlAccess = new MySqlDbAccess();
        try (Connection conn = mysqlAccess.getConnection(
                mysql.getContainerIpAddress(), mysql.getMappedPort(port))) {
            mysqlAccess.createTable(conn);
        }
    }

    @Test
    public void testInsert() throws Exception {
        MySqlDbAccess mysqlAccess = new MySqlDbAccess();
        try (Connection conn = mysqlAccess.getConnection(
                mysql.getContainerIpAddress(), mysql.getMappedPort(port))) {
            Data insertedData = mysqlAccess.insert(conn, "TestInsert");
            assertThat("挿入した行を取得できる",
                    insertedData, is(notNullValue()));
            assertTrue("挿入した ID 列を取得できる",
                    insertedData.id > 0);
            assertThat("MySQL では挿入した値は取得できない",
                    insertedData.value, is(nullValue()));
        }
    }

    @Test
    public void testUpdate() throws Exception {
        MySqlDbAccess mysqlAccess = new MySqlDbAccess();
        try (Connection conn = mysqlAccess.getConnection(
                mysql.getContainerIpAddress(), mysql.getMappedPort(port))) {
            Data inserted = mysqlAccess.insert(conn, "Insert");
            Data updated = mysqlAccess.update(conn, inserted.id, "TestUpdate");
            assertThat("MySQL では更新された行は取得できない",
                    updated, is(nullValue()));
        }
    }
}
