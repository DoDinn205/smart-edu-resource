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

import com.paq.pojo.request.ReqCategoryDTO;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.TopicService;

import jakarta.validation.Valid;

@RestController
public class ApiTopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/api/topics")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getTopics(@RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách chủ đề thành công");
        res.setData(this.topicService.getTopics(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/api/topics/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getTopicById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin chủ đề thành công");
        res.setData(this.topicService.getTopicById(id));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/api/secure/topics")
    public ResponseEntity<ResResponse<ResCategoryDTO>> createTopic(@Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo chủ đề thành công");
        res.setData(this.topicService.createTopic(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/api/secure/topics/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> updateTopic(@PathVariable int id,
            @Valid @RequestBody ReqCategoryDTO request) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật chủ đề thành công");
        res.setData(this.topicService.updateTopic(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/api/secure/topics/{id}")
    public ResponseEntity<ResResponse<Object>> deleteTopic(@PathVariable int id) {
        this.topicService.deleteTopic(id);
        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa chủ đề thành công");
        return ResponseEntity.ok(res);
    }
}
