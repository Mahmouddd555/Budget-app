package database;

import model.Notification;
import java.util.List;

public class NotificationDAO {
    public void save(Notification notification, int userId) {}
    public List<Notification> findByUserId(int userId) {}
    public void markAsRead(int notificationId) {}
}