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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is a servlet which responds to post requests when a user enters text into the message input of the home
 * page and clicks the submit button.
 */
public class LoggingServlet extends HttpServlet {
  private Logger log = LoggerFactory.getLogger(LoggingServlet.class);
  // format for time (ex. 12:34:56 PM)
  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
  private Connection database;

  /**
   * Responds to a post request, receiving the string that was input by a user and adding it to the sqlite database.
   * Then responds back to the home page to update the list of messages.
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    try {
      database = DriverManager.getConnection("jdbc:sqlite:src/main/webapp/WEB-INF/log.db");

      newMessage(database, request); // Inserts new log message into sqlite db

      // Redirect back to home page
      response.sendRedirect("http://localhost:8080/home");

    } catch (SQLException e) {
      log.error("Failed to insert new log message into sqlDB.");
    }

  }

  /**
   * Inserts a new record into the sqlite database using the form data from the http request.
   *
   * @param connection to the sqlite database
   * @param request    the value to insert into the database
   * @throws SQLException in case of failure to insert a new record
   */
  private void newMessage(Connection connection, HttpServletRequest request) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("INSERT INTO log(TIME, MESSAGE) VALUES(?, ?)");
    LocalTime now = LocalTime.now(); // Gets local time of the user at the time the record is created

    String time = now.format(formatter);

    ps.setString(1, time);
    ps.setString(2, request.getParameter("msg"));

    ps.executeUpdate();
  }

}
