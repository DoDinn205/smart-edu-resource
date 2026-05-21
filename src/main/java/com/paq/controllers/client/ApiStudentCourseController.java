/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.Course;
import com.paq.service.StudentCourseService;
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
@RequestMapping("api/student")
@CrossOrigin
public class ApiStudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    @GetMapping("/courses")
    public ResponseEntity<?> getCourses() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return ResponseEntity.ok(this.studentCourseService.getCourses()
                .stream()
                .map(c -> courseToMap(c))
                .collect(Collectors.toList()));

    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<?> getCourseDetail(@PathVariable(value = "id") int id) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Course c = this.studentCourseService.getCourseById(id);

        if (c == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(c);
    }

    @PostMapping("/courses/{id}/enroll")
    public ResponseEntity<?> enrollCourse(
            @PathVariable(value = "id") int id, Principal principal) {
        try {
            return ResponseEntity.ok(this.studentCourseService.enrollCourse(principal.getName(), id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyCourses(Principal principal) {
        try {
            return ResponseEntity.ok(
                    this.studentCourseService.getMyCourses(principal.getName())
            );
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    private Map<String, Object> courseToMap(Course c) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, Object> m = new HashMap<>();
        m.put("id", c.getId());
        m.put("name", c.getName());
        m.put("description", c.getDescription());
        m.put("startDate", c.getStartDate() != null ? f.format(c.getStartDate()) : null);
        m.put("endDate", c.getEndDate() != null ? f.format(c.getEndDate()) : null);
        m.put("isPaid", c.getIsPaid());
        m.put("targetLevel", c.getTargetLevel());
        return m;
    }
}
