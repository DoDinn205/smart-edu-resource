package com.paq.controllers.client;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqSubjectDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.pojo.response.ResSubjectDTO;
import com.paq.service.SubjectService;

import jakarta.validation.Valid;

@RestController
public class ApiSubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping("/api/subjects")
    public ResponseEntity<ResResponse<List<ResSubjectDTO>>> getSubjects(@RequestParam Map<String, String> params) {
        ResResponse<List<ResSubjectDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách môn học thành công");
        res.setData(this.subjectService.getSubjects(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/subjects/{id}")
    public ResponseEntity<ResResponse<ResSubjectDTO>> getSubjectById(@PathVariable int id) {
        ResResponse<ResSubjectDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin môn học thành công");
        res.setData(this.subjectService.getSubjectById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/secure/subjects")
    public ResponseEntity<ResResponse<ResSubjectDTO>> createSubject(@Valid @RequestBody ReqSubjectDTO request) {
        ResResponse<ResSubjectDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo môn học thành công");
        res.setData(this.subjectService.createSubject(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/api/secure/subjects/{id}")
    public ResponseEntity<ResResponse<ResSubjectDTO>> updateSubject(@PathVariable int id,
            @Valid @RequestBody ReqSubjectDTO request) {
        ResResponse<ResSubjectDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật môn học thành công");
        res.setData(this.subjectService.updateSubject(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/api/secure/subjects/{id}")
    public ResponseEntity<ResResponse<Object>> deleteSubject(@PathVariable int id) {
        this.subjectService.deleteSubject(id);
        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa môn học thành công");
        return ResponseEntity.ok(res);
    }
}
