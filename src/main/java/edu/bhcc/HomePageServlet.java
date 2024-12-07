package edu.bhcc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * This class is a servlet which handles all get requests for the home page @ {@code http://localhost:8080/home}.
 */
public class HomePageServlet extends HttpServlet {
  private Logger log = LoggerFactory.getLogger(HomePageServlet.class);
  private Connection database;

  /**
   * Processes a get request and displays the home page of the webapp.
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
        log.error("Unable to process template");
      }

    } catch (SQLException e) {
      log.error("Error when attempting to get records from sqlDB.");
    }


  }

  /**
   * Queries the sqlite database for all records and stores them within an ArrayList of MessageRecords.
   *
   * @param connection the database connection
   * @return an ArrayList of all records within the database
   * @throws SQLException in cases of sql errors.
   */
  private ArrayList<MessageRecord> getMessages(Connection connection) throws SQLException {
    ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM log");
    ArrayList<MessageRecord> messages = new ArrayList<>();

    while (resultSet.next()) {
      String time = resultSet.getString("TIME");
      String msg = resultSet.getString("MESSAGE");

      messages.add(new MessageRecord(time, msg));
    }
    
    return messages;
  }
}
