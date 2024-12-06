package edu.bhcc;

public class MessageRecord {
  private String time;
  private String message;

  MessageRecord(String time, String message) {
    this.time = time;
    this.message = message;
  }

  public String getTime() {
    return time;
  }

  public String getMessage() {
    return message;
  }
}
