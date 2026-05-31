/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.Notification;
import com.paq.pojo.response.ResNotificationDTO;
import com.paq.repository.NotificationRepository;
import com.paq.service.StudentNotificationService;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StudentNotificationServiceImpl implements StudentNotificationService {

    private static final SimpleDateFormat DATETIME_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private NotificationRepository notificationRepo;

    @Override
    public List<ResNotificationDTO> getMyNotifications(String username) {
        return this.notificationRepo.getNotificationsByUsername(username)
                .stream()
                .map(n -> this.toDTO(n))
                .collect(Collectors.toList());
    }

    @Override
    public ResNotificationDTO markAsRead(String username, int notificationId) {
        Notification n = this.notificationRepo.getNotificationById(notificationId);

        this.validateOwner(username, n);

        n.setIsRead(true);

        return this.toDTO(this.notificationRepo.updateNotification(n));
    }

    @Override
    public void markAllAsRead(String username) {
        List<Notification> notifications
                = this.notificationRepo.getNotificationsByUsername(username);

        for (Notification n : notifications) {
            n.setIsRead(true);
            this.notificationRepo.updateNotification(n);
        }
    }

    @Override
    public ResNotificationDTO deleteNotification(String username, int notificationId) {
        Notification n = this.notificationRepo.getNotificationById(notificationId);

        this.validateOwner(username, n);

        n.setIsDeleted(true);

        return this.toDTO(this.notificationRepo.updateNotification(n));
    }

    private void validateOwner(String username, Notification n) {
        if (n == null || Boolean.TRUE.equals(n.getIsDeleted())) {
            throw new IdInvalidException("Notification không tồn tại");
        }

        if (n.getUserId() == null || !n.getUserId().getUsername().equals(username)) {
            throw new PermissionException("Bạn không có quyền thao tác thông báo này");
        }
    }

    private ResNotificationDTO toDTO(Notification n) {
        ResNotificationDTO dto = new ResNotificationDTO();

        dto.setId(n.getId());
        dto.setTitle(n.getTitle());
        dto.setContent(n.getContent());
        dto.setIsRead(n.getIsRead());

        if (n.getCreatedAt() != null) {
            dto.setCreatedAt(DATETIME_FORMAT.format(n.getCreatedAt()));
        }

        return dto;
    }

}
