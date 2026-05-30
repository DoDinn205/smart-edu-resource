package com.paq.controllers.lecturer;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqCourseDTO;
import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResCourseDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.CourseService;
import com.paq.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/courses")
    public ResponseEntity<ResResponse<ResCourseDTO>> createCourse(@Valid @RequestBody ReqCourseDTO request) {
        ResResponse<ResCourseDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo khóa học thành công");
        res.setData(this.courseService.createCourse(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/courses/{id}")
    public ResponseEntity<ResResponse<ResCourseDTO>> updateCourse(@PathVariable int id,
            @Valid @RequestBody ReqCourseDTO request) {
        ResResponse<ResCourseDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật khóa học thành công");
        res.setData(this.courseService.updateCourse(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<ResResponse<Object>> deleteCourse(@PathVariable int id) {
        this.courseService.deleteCourse(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa khóa học thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/courses/{courseId}/enrollments")
    public ResponseEntity<ResResponse<List<ResEnrollmentDTO>>> getEnrollmentsByCourseId(
            @PathVariable int courseId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResEnrollmentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách đăng ký của khóa học thành công");
        res.setData(this.enrollmentService.getEnrollmentsByCourseId(courseId, params));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/enrollments/{id}/status")
    public ResponseEntity<ResResponse<ResEnrollmentDTO>> updateEnrollmentStatus(
            @PathVariable int id,
            @Valid @RequestBody ReqEnrollmentStatusDTO request) {
        ResResponse<ResEnrollmentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái đăng ký thành công");
        res.setData(this.enrollmentService.updateEnrollmentStatus(id, request));

        return ResponseEntity.ok(res);
    }
}
