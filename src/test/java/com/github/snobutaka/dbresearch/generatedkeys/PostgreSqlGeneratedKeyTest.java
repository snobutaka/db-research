package com.github.snobutaka.dbresearch.generatedkeys;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import com.github.snobutaka.dbresearch.generatedkeys.Data;
import com.github.snobutaka.dbresearch.generatedkeys.PostgreSqlDbAccess;

public class PostgreSqlGeneratedKeyTest {
    static final int port = 5432;

    @ClassRule
    public static GenericContainer postgres = new GenericContainer<>("postgres:10")
            .withExposedPorts(port);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Thread.sleep(2 * 1000); // DB 起動を待つ

        PostgreSqlDbAccess postgresAccess = new PostgreSqlDbAccess();
        try (Connection conn = postgresAccess.getConnection(
                postgres.getContainerIpAddress(),  postgres.getMappedPort(port))) {
            postgresAccess.createTable(conn);
        }
    }

    @Test
    public void testInsert() throws Exception {
        PostgreSqlDbAccess postgresAccess = new PostgreSqlDbAccess();
        try (Connection conn = postgresAccess.getConnection(
                postgres.getContainerIpAddress(), postgres.getMappedPort(port))) {
            Data insertedData = postgresAccess.insert(conn, "TestInsert");
            assertThat("挿入した行を取得できる",
                    insertedData, is(notNullValue()));
            assertTrue("挿入した ID 列を取得できる",
                    insertedData.id > 0);
            assertThat("PostgreSQL では挿入した値も取得できる",
                    insertedData.value, is("TestInsert"));
        }
    }

    @Test
    public void testUpdate() throws Exception {
        PostgreSqlDbAccess postgresAccess = new PostgreSqlDbAccess();
        try (Connection conn = postgresAccess.getConnection(
                postgres.getContainerIpAddress(), postgres.getMappedPort(port))) {

            Data inserted = postgresAccess.insert(conn, "Insert");
            Data updated = postgresAccess.update(conn, inserted.id, "TestUpdate");
            assertThat("Postgres では更新した行が取得できる",
                    updated, is(notNullValue()));
            assertThat("更新された行は挿入した行と ID が一致する",
                    updated.id, is(inserted.id));
            assertThat("更新された値が取得できる",
                    updated.value, is("TestUpdate"));
        }
    }
}
