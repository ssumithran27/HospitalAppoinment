package org.example.dao;

import org.example.configs.DBConnection;
import org.example.exception.MyClassException;
import org.example.model.LoggingUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoggingDAO {
    private static final Logger log = LoggerFactory.getLogger(LoggingDAO.class);

    static final int LOGGING_NAME = 1;
    static final int LOGGING_PASSWORD = 2;

    private static final int GET_PASSWORD = 1;


    String Sql = "Insert into users(username,password)" +
            "VALUES(?,?)";

     String GET_PASS = " select password from users where username = ?";


    public void loggingUser(LoggingUser loggingUser) {

        try (Connection con = DBConnection.getConnect().getConnection();
             PreparedStatement ps = con.prepareStatement(Sql)) {

            ps.setString(LOGGING_NAME, loggingUser.getLoggingName());
            ps.setString(LOGGING_PASSWORD, loggingUser.getLoggingPassword());

            int changedRows = ps.executeUpdate();

            if (changedRows == 0) {
                log.error("Insert failed,fields are not inserted in logging user:{}", loggingUser.getLoggingId());
            } else {
                log.info("successfully values are inserted in account:{}", loggingUser.getLoggingId());
            }

        } catch (SQLException e) {
            throw new MyClassException("failed to add", e);
        }
    }

    public  String getPass(String username) {
        try (Connection con = DBConnection.getConnect().getConnection();
             PreparedStatement ps = con.prepareStatement(GET_PASS)) {
            ps.setString(GET_PASSWORD, username);
            ResultSet rs = ps.executeQuery();
            log.info("Query Executed");
            if (rs.next()) {
                return rs.getString("password");
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new MyClassException("Failed to get Password ", e);
        }


    }
}
