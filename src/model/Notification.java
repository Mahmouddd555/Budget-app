package model;

import java.time.LocalDateTime;

public class Notification {

    private int id;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;

    public Notification(int id, String message) {
        this.id = id;
        this.message = message;
        this.isRead = false;
        this.timestamp = LocalDateTime.now();
    }

    public void markAsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }
    public boolean isRead() {
        return isRead;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public int getId() { return id; }
}