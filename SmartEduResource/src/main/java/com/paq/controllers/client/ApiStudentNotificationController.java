/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.response.ResNotificationDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.StudentNotificationService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/secure/student/notifications")
@CrossOrigin
public class ApiStudentNotificationController {
    
    @Autowired
    private StudentNotificationService notificationService;
    @GetMapping
    public ResponseEntity<ResResponse<List<ResNotificationDTO>>> getMyNotifications(
            Principal principal) {

        ResResponse<List<ResNotificationDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách thông báo thành công");
        res.setData(this.notificationService.getMyNotifications(principal.getName()));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<ResResponse<ResNotificationDTO>> markAsRead(
            @PathVariable("id") int id,
            Principal principal) {

        ResResponse<ResNotificationDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Đánh dấu đã đọc thành công");
        res.setData(this.notificationService.markAsRead(principal.getName(), id));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/read-all")
    public ResponseEntity<ResResponse<Void>> markAllAsRead(Principal principal) {
        this.notificationService.markAllAsRead(principal.getName());

        ResResponse<Void> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Đánh dấu tất cả đã đọc thành công");
        res.setData(null);

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResResponse<ResNotificationDTO>> deleteNotification(
            @PathVariable("id") int id,
            Principal principal) {

        ResResponse<ResNotificationDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa thông báo thành công");
        res.setData(this.notificationService.deleteNotification(principal.getName(), id));

        return ResponseEntity.ok(res);
    }
    
}
