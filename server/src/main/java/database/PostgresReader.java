package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class PostgresReader<T> {
    public abstract List<T> readObjects(Connection connection) throws SQLException;
}