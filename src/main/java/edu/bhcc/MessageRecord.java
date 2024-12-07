package edu.bhcc;

/**
 * A simple record class for storing data about log messages between the sqlite database and the web app.
 */
public class MessageRecord {
  private String time;
  private String message;

  /**
   * Constructor for the class, sets both class attributes.
   *
   * @param time    when the message was input according to user's local time
   * @param message the content of the message
   */
  MessageRecord(String time, String message) {
    this.time = time;
    this.message = message;
  }

  /**
   * @return the time a message was saved
   */
  public String getTime() {
    return time;
  }

  /**
   * @return the message content
   */
  public String getMessage() {
    return message;
  }
}
