package com.paq.controllers.lecturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqCourseLessonDTO;
import com.paq.pojo.response.ResCourseLessonDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.CourseLessonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerLessonController {

    @Autowired
    private CourseLessonService lessonService;

    @GetMapping("/courses/{courseId}/lessons")
    public ResponseEntity<ResResponse<List<ResCourseLessonDTO>>> getLessonsByCourse(@PathVariable int courseId) {
        ResResponse<List<ResCourseLessonDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách bài học thành công");
        res.setData(this.lessonService.getLessonsByCourseId(courseId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/lessons")
    public ResponseEntity<ResResponse<ResCourseLessonDTO>> createLesson(
            @Valid @RequestBody ReqCourseLessonDTO request) {
        ResResponse<ResCourseLessonDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo bài học thành công");
        res.setData(this.lessonService.createLesson(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<ResResponse<ResCourseLessonDTO>> updateLesson(
            @PathVariable int id,
            @Valid @RequestBody ReqCourseLessonDTO request) {
        ResResponse<ResCourseLessonDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật bài học thành công");
        res.setData(this.lessonService.updateLesson(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<ResResponse<Object>> deleteLesson(@PathVariable int id) {
        this.lessonService.deleteLesson(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa bài học thành công");

        return ResponseEntity.ok(res);
    }
}
