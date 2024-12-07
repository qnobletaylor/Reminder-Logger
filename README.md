# Message Logging | Adv Java HW7

This web app allows a user to input text and submit it as a log message to the server. When a log message is
submitted it will be added as a record to a SQLite database on the backend. Also when a log message is submitted the
server will capture the local time and save that timestamp alongside the message.

This app uses maven, as well as the jetty plugin. To start the locally hosted server use `mvn jetty:run` while in
the root folder and then navigate to [localhost:8080/home](http://localhost:8080/home) on your preferred browser.

#### Valid addresses :

- `localhost:8080/home`

### SQLite table creation statement :

```sqlite
CREATE TABLE "log"
(
    "ID"      INTEGER NOT NULL,
    "TIME"    TEXT    NOT NULL,
    "MESSAGE" TEXT    NOT NULL,
    PRIMARY KEY ("ID" AUTOINCREMENT)
);
```