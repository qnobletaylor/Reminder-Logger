package edu.bhcc;

public class MessageRecord {
  private int id;
  private String time;
  private String message;

  MessageRecord(int id, String time, String message) {
    this.id = id;
    this.time = time;
    this.message = message;
  }

  public int getId() {
    return id;
  }

  public String getTime() {
    return time;
  }

  public String getMessage() {
    return message;
  }
}
