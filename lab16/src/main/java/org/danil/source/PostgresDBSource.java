package org.danil.source;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresDBSource implements Source {
    private final Connection connection;
    private final String name;

    public PostgresDBSource(Connection connection, String name) {
        this.connection = connection;
        this.name = name;
        try {
            final var stmt = connection.createStatement();
            // !!! preparedStatement нельзя использовать для подстановки имени таблицы, поэтому интерполяция
            stmt.executeUpdate("""
                    create table IF NOT EXISTS %s (
                        args text primary key not null,
                        result text not null
                    )
                    """.formatted(name));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void put(String args, String result) {
        try {
            final var pstmt = connection.prepareStatement("""
                    insert into %s (args, result) values (?, ?);
                    """.formatted(name));
            pstmt.setString(1, args);
            pstmt.setString(2, result);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String get(String args) {
        try {
            final var pstmt = connection.prepareStatement("""
                    select result from %s where args = ?;
                    """.formatted(name));
            pstmt.setString(1, args);
            final var resultSet = pstmt.executeQuery();
            if (resultSet.next())
                return resultSet.getString("result");
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
