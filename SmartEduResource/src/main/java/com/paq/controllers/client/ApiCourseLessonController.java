package com.paq.controllers.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResCourseLearnDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.CourseLessonService;

@RestController
public class ApiCourseLessonController {

    @Autowired
    private CourseLessonService lessonService;

    @GetMapping("/api/secure/student/courses/{courseId}/learn")
    public ResponseEntity<ResResponse<ResCourseLearnDTO>> getLearnPage(@PathVariable int courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth != null ? auth.getName() : null;

        ResResponse<ResCourseLearnDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy trang học thành công");
        res.setData(this.lessonService.getLearnPage(courseId, username));

        return ResponseEntity.ok(res);
    }
}
