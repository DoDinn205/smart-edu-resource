package com.paq.controllers.client;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.User;
import com.paq.pojo.request.ReqLecturerDTO;
import com.paq.pojo.request.ReqLoginDTO;
import com.paq.pojo.request.ReqRegisterDTO;
import com.paq.pojo.request.ReqStudentRegisterDTO;
import com.paq.pojo.response.ResLecturerDTO;
import com.paq.pojo.response.ResLoginDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResStudentDTO;
import com.paq.pojo.response.ResUserDTO;
import com.paq.service.UserService;
import com.paq.utils.DTOMapper;
import com.paq.utils.JwtUtils;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResResponse<ResUserDTO>> register(@Valid @RequestBody ReqRegisterDTO request) {
        User user = this.userService.addUser(request);

        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Đăng ký thành công");
        res.setData(DTOMapper.toResUserDTO(user));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(path = "/register/student", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResResponse<ResStudentDTO>> registerStudent(
            @Valid @RequestBody ReqStudentRegisterDTO request) {
        ResResponse<ResStudentDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Đăng ký sinh viên thành công");
        res.setData(this.userService.registerStudent(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping(path = "/register/lecturer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResResponse<ResLecturerDTO>> registerLecturer(
            @Valid @RequestBody ReqLecturerDTO request) {
        ResResponse<ResLecturerDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Dang ky giang vien thanh cong, vui long cho admin duyet");
        res.setData(this.userService.registerLecturer(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ResResponse<ResLoginDTO>> login(@Valid @RequestBody ReqLoginDTO req) throws Exception {
        if (!this.userService.authenticate(req.getUsername(), req.getPassword())) {
            throw new BadCredentialsException("Sai thông tin đăng nhập");
        }

        User user = this.userService.getUserByUsername(req.getUsername());
        String accessToken = this.jwtUtils.generateToken(user.getUsername());
        String role = user.getRole() != null ? user.getRole().name() : null;
        ResLoginDTO resLogin = new ResLoginDTO(accessToken, user.getId(), user.getUsername(), role);

        ResResponse<ResLoginDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Đăng nhập thành công");
        res.setData(resLogin);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/profile")
    public ResponseEntity<ResResponse<ResUserDTO>> getProfile(Principal principal) {
        if (principal == null) {
            throw new AuthenticationCredentialsNotFoundException("User chưa đăng nhập");
        }

        User user = this.userService.getUserByUsername(principal.getName());

        ResResponse<ResUserDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin người dùng thành công");
        res.setData(DTOMapper.toResUserDTO(user));

        return ResponseEntity.ok(res);
    }
}
