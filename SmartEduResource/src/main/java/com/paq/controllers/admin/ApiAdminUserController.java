package com.paq.controllers.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResStudentDTO;
import com.paq.pojo.response.ResUserDTO;
import com.paq.service.UserService;
import com.paq.utils.DTOMapper;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/admin")
public class ApiAdminUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment env;

    @GetMapping("/users")
    public ResponseEntity<ResResponse<ResPageDTO<ResUserDTO>>> getUsers(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("users.page_size", Integer.class);

        Map<String, String> countParams = new HashMap<>(params);
        Long totalItems = this.userService.countUsers(countParams);

        ResPageDTO<ResUserDTO> pageDTO = DTOMapper.toResPageDTO(this.userService.getUsers(params), totalItems, page, pageSize);

        ResResponse<ResPageDTO<ResUserDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách người dùng thành công");
        res.setData(pageDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ResResponse<ResUserDTO>> getUserById(@PathVariable int id) {
        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết người dùng thành công");
        res.setData(this.userService.getUserById(id));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<ResResponse<ResUserDTO>> updateUserStatus(@PathVariable int id,
            @Valid @RequestBody ReqUserStatusDTO request) {
        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái tài khoản thành công");
        res.setData(this.userService.updateUserStatus(id, request));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/students")
    public ResponseEntity<ResResponse<ResPageDTO<ResStudentDTO>>> getStudents(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("users.page_size", Integer.class);

        Map<String, String> countParams = new HashMap<>(params);
        Long totalItems = this.userService.countStudents(countParams);

        ResPageDTO<ResStudentDTO> pageDTO = DTOMapper.toResPageDTO(
                this.userService.getStudents(params), totalItems, page, pageSize);

        ResResponse<ResPageDTO<ResStudentDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách sinh viên thành công");
        res.setData(pageDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ResResponse<ResStudentDTO>> getStudentById(@PathVariable int id) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết sinh viên thành công");
        res.setData(this.userService.getStudentById(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/students")
    public ResponseEntity<ResResponse<ResStudentDTO>> createStudent(@Valid @RequestBody ReqStudentDTO request) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo sinh viên thành công");
        res.setData(this.userService.createStudent(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<ResResponse<ResStudentDTO>> updateStudent(@PathVariable int id,
            @Valid @RequestBody ReqStudentDTO request) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật sinh viên thành công");
        res.setData(this.userService.updateStudent(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<ResResponse<Object>> deleteStudent(@PathVariable int id) {
        this.userService.deleteStudent(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Khóa tài khoản sinh viên thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/lecturers")
    public ResponseEntity<ResResponse<ResPageDTO<ResLecturerDTO>>> getLecturers(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("users.page_size", Integer.class);

        Map<String, String> countParams = new HashMap<>(params);
        Long totalItems = this.userService.countLecturers(countParams);

        ResPageDTO<ResLecturerDTO> pageDTO = DTOMapper.toResPageDTO(
                this.userService.getLecturers(params), totalItems, page, pageSize);

        ResResponse<ResPageDTO<ResLecturerDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách giảng viên thành công");
        res.setData(pageDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/lecturers/{id}")
    public ResponseEntity<ResResponse<ResLecturerDTO>> getLecturerById(@PathVariable int id) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết giảng viên thành công");
        res.setData(this.userService.getLecturerById(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/lecturers")
    public ResponseEntity<ResResponse<ResLecturerDTO>> createLecturer(@Valid @RequestBody ReqLecturerDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo giảng viên thành công");
        res.setData(this.userService.createLecturer(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/lecturers/{id}")
    public ResponseEntity<ResResponse<ResLecturerDTO>> updateLecturer(@PathVariable int id,
            @Valid @RequestBody ReqLecturerDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật giảng viên thành công");
        res.setData(this.userService.updateLecturer(id, request));

        return ResponseEntity.ok(res);
    }

    @PutMapping("/lecturers/{id}/approval")
    public ResponseEntity<ResResponse<ResLecturerDTO>> updateLecturerApproval(@PathVariable int id,
            @Valid @RequestBody ReqLecturerApprovalDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật trạng thái duyệt giảng viên thành công");
        res.setData(this.userService.updateLecturerApproval(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/lecturers/{id}")
    public ResponseEntity<ResResponse<Object>> deleteLecturer(@PathVariable int id) {
        this.userService.deleteLecturer(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Khóa tài khoản giảng viên thành công");

        return ResponseEntity.ok(res);
    }
}
