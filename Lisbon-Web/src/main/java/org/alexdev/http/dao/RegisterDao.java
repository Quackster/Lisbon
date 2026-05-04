package org.alexdev.http.dao;

import net.h4bbo.lisbon.dao.Storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterDao {
    public static int newUser(String username, String password, String figure, String gender, String email, String birthday) throws SQLException {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        int userId = -1;

        try {
            sqlConnection = Storage.getStorage().getConnection();
            preparedStatement = Storage.getStorage().prepare("INSERT INTO users (username, password, figure, sex, pool_figure, sso_ticket, email, birthday) VALUES (?, ?, ?, ?, '', '', ?, ?)", sqlConnection);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, figure);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, birthday);
            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                userId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Storage.logError(e);
            throw e;
        } finally {
            Storage.closeSilently(resultSet);
            Storage.closeSilently(preparedStatement);
            Storage.closeSilently(sqlConnection);
        }

        return userId;
    }
}
