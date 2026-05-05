package database;

import model.Notification;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationDAO {
    public void save(Notification notification, int userId) {
        Database.notifications.add(notification);
    }
    public List<Notification> findByUserId(int userId) {
        return Database.notifications.stream().filter(notification -> notification.getId() == userId).collect(Collectors.toList());
    }
    public void markAsRead(int notificationId) {
        Notification notification = Database.notifications.stream().filter(n -> n.getId() == notificationId).findFirst().orElse(null);
        if (notification != null) {
            notification.markAsRead(true);
        }
    }
}