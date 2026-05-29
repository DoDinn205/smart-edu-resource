package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/secure/courses/{courseId}/enrollments")
    public ResponseEntity<ResResponse<List<ResEnrollmentDTO>>> getEnrollmentsByCourseId(
            @PathVariable int courseId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResEnrollmentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách đăng ký của khóa học thành công");
        res.setData(this.enrollmentService.getEnrollmentsByCourseId(courseId, params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/my-enrollments")
    public ResponseEntity<ResResponse<List<ResEnrollmentDTO>>> getMyEnrollments() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ResResponse<List<ResEnrollmentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách khóa học của tôi thành công");
        res.setData(this.enrollmentService.getMyEnrollments(auth != null ? auth.getName() : null));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/courses/{courseId}/enroll")
    public ResponseEntity<ResResponse<ResEnrollmentDTO>> enrollSelf(@PathVariable int courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ResResponse<ResEnrollmentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Đăng ký khóa học thành công");
        res.setData(this.enrollmentService.enrollSelf(courseId, auth != null ? auth.getName() : null));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/enrollments/{id}/status")
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
