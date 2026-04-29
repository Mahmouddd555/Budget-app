package model;

import java.time.LocalDateTime;

public class Notification {

    private int id;
    private String message;
    private boolean isRead;
    private LocalDateTime timestamp;

    public Notification(int id, String message) {}

    public void markAsRead() {}

    public String getMessage() {
        return message;
    }
}