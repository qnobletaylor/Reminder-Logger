package edu.bhcc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello, World Servlet.
 */
public class HomePageServlet extends HttpServlet {

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
      root.put("msgList", getMessages(database));

      // Get the FreeMarker Template Engine
      FreeMarkerUtil setup = FreeMarkerUtil.getInstance();
      Configuration cfg = setup.getFreeMarkerConfiguration();
      Template template = cfg.getTemplate("index.html");

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

  private ArrayList<MessageRecord> getMessages(Connection connection) throws SQLException {
    ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM log");
    ArrayList<MessageRecord> messages = new ArrayList<>();

    if (resultSet.next()) {
      do {
        int id = resultSet.getInt("ID");
        String time = resultSet.getString("TIME");
        String msg = resultSet.getString("MESSAGE");

        messages.add(new MessageRecord(id, time, msg));
      } while (resultSet.next());
    }
    return messages;
  }
}
