package edu.bhcc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello, World Servlet.
 */
public class HelloServlet extends HttpServlet {
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
  private Connection database;

  /**
   * Process an HTTP Request.
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);

    try {
      database = DriverManager.getConnection("jdbc:sqlite:src/main/webapp/WEB-INF/log.db");
      
      // Create the Model
      Map<String, Object> root = new HashMap<>();
      root.put("msgList", getMessages(database, request));

      // Get the FreeMarker Template Engine
      FreeMarkerUtil setup = FreeMarkerUtil.getInstance();
      Configuration cfg = setup.getFreeMarkerConfiguration();
      Template template = cfg.getTemplate("hello.html");

      //  Merge the Model with the Template
      PrintWriter writer = response.getWriter();

      try {
        template.process(root, writer);
      } catch (TemplateException e) {
        writer.println("Could not process template:  " + e.getMessage());
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }


  }

  private void newMessage(Connection connection, HttpServletRequest request) throws SQLException {
    PreparedStatement ps = connection.prepareStatement("INSERT INTO log VALUES(?, ?)");
    LocalTime now = LocalTime.now();
    String time = now.format(formatter);

    ps.setString(1, time);
    ps.setString(2, request.getParameter("msg"));
  }

  private ArrayList<LogMsg> getMessages(Connection connection, HttpServletRequest request) throws SQLException {
    ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM log");
    ArrayList<LogMsg> messages = new ArrayList<>();

    if (resultSet.next()) {
      do {
        String time = resultSet.getString("TIME");
        String msg = resultSet.getString("MESSAGE");

        messages.add(new LogMsg(time, msg));
      } while (resultSet.next());
    }
    return messages;
  }

  private record LogMsg(String time, String message) {
  }
}
