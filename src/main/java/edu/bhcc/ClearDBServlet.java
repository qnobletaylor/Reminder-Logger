package edu.bhcc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClearDBServlet extends HttpServlet {
  private Connection database;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    try {
      database = DriverManager.getConnection("jdbc:sqlite:src/main/webapp/WEB-INF/log.db");

      PreparedStatement ps = database.prepareStatement("DELETE FROM log");

      ps.executeUpdate();

      response.sendRedirect("http://localhost:8080/home");

    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

  }
}
