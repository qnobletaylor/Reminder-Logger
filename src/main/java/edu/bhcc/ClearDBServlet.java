package edu.bhcc;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * This class acts as a servlet to handle post requests sent from the home page of the log message app. Specifically
 * when the clear button is interacted with on the webpage this class handles clearing the sqlite database of all
 * records.
 */
public class ClearDBServlet extends HttpServlet {
  private Logger log = LoggerFactory.getLogger(ClearDBServlet.class);
  private Connection database;

  /**
   * Post request handling, clears all records from the database and redirects the response back to {@code
   * HomePageServlet}
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    try {
      // Create database connection
      database = DriverManager.getConnection("jdbc:sqlite:src/main/webapp/WEB-INF/log.db");

      // Statement to delete all records from db
      PreparedStatement ps = database.prepareStatement("DELETE FROM log");
      ps.executeUpdate();

      // Redirects back to homepage
      response.sendRedirect("http://localhost:8080/home");

    } catch (SQLException e) {
      log.warn("Failed to delete all records from sqlDB.");
    }

  }
}
