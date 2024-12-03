package edu.bhcc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LoggingServlet extends HttpServlet {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
  private Connection database;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    try {
      database = DriverManager.getConnection("jdbc:sqlite:src/main/webapp/WEB-INF/log.db");

      newMessage(database, request);

      response.sendRedirect("http://localhost:8080/home");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }

  private void newMessage(Connection connection, HttpServletRequest request) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("INSERT INTO log(TIME, MESSAGE) VALUES(?, ?)");
    LocalTime now = LocalTime.now();
    String time = now.format(formatter);

    ps.setString(1, time);
    ps.setString(2, request.getParameter("msg"));

    ps.executeUpdate();
  }

}
