package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResCourseDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.CourseService;

@RestController
public class ApiCourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/api/courses")
    public ResponseEntity<ResResponse<List<ResCourseDTO>>> getCourses(@RequestParam Map<String, String> params) {
        ResResponse<List<ResCourseDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách khóa học thành công");
        res.setData(this.courseService.getCourses(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/courses/{id}")
    public ResponseEntity<ResResponse<ResCourseDTO>> getCourseById(@PathVariable int id) {
        ResResponse<ResCourseDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin khóa học thành công");
        res.setData(this.courseService.getCourseById(id));

        return ResponseEntity.ok(res);
    }
}
