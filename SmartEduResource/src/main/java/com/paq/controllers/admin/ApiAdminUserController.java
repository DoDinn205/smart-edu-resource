package com.paq.controllers.admin;

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

import com.paq.pojo.request.ReqLecturerApprovalDTO;
import com.paq.pojo.request.ReqLecturerDTO;
import com.paq.pojo.request.ReqStudentDTO;
import com.paq.pojo.request.ReqUserStatusDTO;
import com.paq.pojo.response.ResLecturerDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResStudentDTO;
import com.paq.pojo.response.ResUserDTO;
import com.paq.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiAdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/secure/admin/users")
    public ResponseEntity<ResResponse<List<ResUserDTO>>> getUsers(@RequestParam Map<String, String> params) {
        ResResponse<List<ResUserDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách người dùng thành công");
        res.setData(this.userService.getUsers(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/admin/users/{id}")
    public ResponseEntity<ResResponse<ResUserDTO>> getUserById(@PathVariable int id) {
        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết người dùng thành công");
        res.setData(this.userService.getUserById(id));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/secure/admin/users/{id}/status")
    public ResponseEntity<ResResponse<ResUserDTO>> updateUserStatus(@PathVariable int id,
            @Valid @RequestBody ReqUserStatusDTO request) {
        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái tài khoản thành công");
        res.setData(this.userService.updateUserStatus(id, request));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/admin/students")
    public ResponseEntity<ResResponse<List<ResStudentDTO>>> getStudents(@RequestParam Map<String, String> params) {
        ResResponse<List<ResStudentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách sinh viên thành công");
        res.setData(this.userService.getStudents(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/admin/students/{id}")
    public ResponseEntity<ResResponse<ResStudentDTO>> getStudentById(@PathVariable int id) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết sinh viên thành công");
        res.setData(this.userService.getStudentById(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/admin/students")
    public ResponseEntity<ResResponse<ResStudentDTO>> createStudent(@Valid @RequestBody ReqStudentDTO request) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo sinh viên thành công");
        res.setData(this.userService.createStudent(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/admin/students/{id}")
    public ResponseEntity<ResResponse<ResStudentDTO>> updateStudent(@PathVariable int id,
            @Valid @RequestBody ReqStudentDTO request) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật sinh viên thành công");
        res.setData(this.userService.updateStudent(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/admin/students/{id}")
    public ResponseEntity<ResResponse<Object>> deleteStudent(@PathVariable int id) {
        this.userService.deleteStudent(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Khóa tài khoản sinh viên thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/admin/lecturers")
    public ResponseEntity<ResResponse<List<ResLecturerDTO>>> getLecturers(@RequestParam Map<String, String> params) {
        ResResponse<List<ResLecturerDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách giảng viên thành công");
        res.setData(this.userService.getLecturers(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/admin/lecturers/{id}")
    public ResponseEntity<ResResponse<ResLecturerDTO>> getLecturerById(@PathVariable int id) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết giảng viên thành công");
        res.setData(this.userService.getLecturerById(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/admin/lecturers")
    public ResponseEntity<ResResponse<ResLecturerDTO>> createLecturer(@Valid @RequestBody ReqLecturerDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo giảng viên thành công");
        res.setData(this.userService.createLecturer(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/admin/lecturers/{id}")
    public ResponseEntity<ResResponse<ResLecturerDTO>> updateLecturer(@PathVariable int id,
            @Valid @RequestBody ReqLecturerDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật giảng viên thành công");
        res.setData(this.userService.updateLecturer(id, request));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/secure/admin/lecturers/{id}/approval")
    public ResponseEntity<ResResponse<ResLecturerDTO>> updateLecturerApproval(@PathVariable int id,
            @Valid @RequestBody ReqLecturerApprovalDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái duyệt giảng viên thành công");
        res.setData(this.userService.updateLecturerApproval(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/admin/lecturers/{id}")
    public ResponseEntity<ResResponse<Object>> deleteLecturer(@PathVariable int id) {
        this.userService.deleteLecturer(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Khóa tài khoản giảng viên thành công");

        return ResponseEntity.ok(res);
    }
}
