/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.LearningLog;
import com.paq.service.StudentLearningService;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("/api/secure/student")
@CrossOrigin
public class ApiStudentLearningController {

    @Autowired
    private StudentLearningService learningService;

    @PostMapping("/resources/{id}/start")
    public ResponseEntity<?> startLearning(
            @PathVariable(value = "id") int id,
            Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Bạn cần đăng nhập trước khi sử dụng chức năng này!");
            }
            return ResponseEntity.ok(
                    learningLogToMap(this.learningService.startLearning(principal.getName(), id))
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PostMapping("/resources/{id}/complete")
    public ResponseEntity<?> completeLearning(
            @PathVariable(value = "id") int id,
            Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Bạn cần đăng nhập trước khi sử dụng chức năng này!");
            }
            return ResponseEntity.ok(
                    learningLogToMap(this.learningService.completeLearning(principal.getName(), id))
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/learning/history")
    public ResponseEntity<?> getHistory(Principal principal) {
        try {
            if (principal == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Bạn cần đăng nhập trước khi sử dụng chức năng này!");
            }
            return ResponseEntity.ok(
                    this.learningService.getHistory(principal.getName())
                            .stream()
                            .map(l -> learningLogToMap(l))
                            .collect(Collectors.toList())
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    private Map<String, Object> learningLogToMap(LearningLog l) {
        SimpleDateFormat f = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss");

        Map<String, Object> m = new HashMap<>();
        m.put("id", l.getId());
        m.put("completionStatus", l.getCompletionStatus());
        m.put("startTime", l.getStartTime() != null ? f.format(l.getStartTime()) : null);
        m.put("endTime", l.getEndTime() != null ? f.format(l.getEndTime()) : null);

        if (l.getResourceId() != null) {
            m.put("resourceId", l.getResourceId().getId());
            m.put("resourceTitle", l.getResourceId().getTitle());
        }
        if (l.getEnrollmentId() != null) {
            m.put("enrollmentId", l.getEnrollmentId().getId());
        }
        return m;
    }
}
