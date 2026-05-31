/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.response.ResNotificationDTO;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StudentNotificationService {

    List<ResNotificationDTO> getMyNotifications(String username);

    ResNotificationDTO markAsRead(String username, int notificationId);

    void markAllAsRead(String username);

    ResNotificationDTO deleteNotification(String username, int notificationId);
}
