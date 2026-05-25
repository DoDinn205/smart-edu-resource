package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.EnrollmentService;

import jakarta.validation.Valid;

@RestController
public class ApiEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/api/secure/courses/{courseId}/enrollments")
    public ResponseEntity<ResResponse<List<ResEnrollmentDTO>>> getEnrollmentsByCourseId(
            @PathVariable int courseId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResEnrollmentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách đăng ký của khóa học thành công");
        res.setData(this.enrollmentService.getEnrollmentsByCourseId(courseId, params));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/api/secure/enrollments/{id}/status")
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
