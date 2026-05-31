/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Notification;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface NotificationRepository {
    List<Notification> getNotificationsByUsername(String username);

    Notification getNotificationById(int id);

    Notification updateNotification(Notification notification);
}
