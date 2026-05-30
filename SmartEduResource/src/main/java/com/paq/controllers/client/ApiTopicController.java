package com.paq.controllers.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.TopicService;

@RestController
@RequestMapping("/api")
public class ApiTopicController {

    @Autowired
    private TopicService topicService;

    @GetMapping("/topics")
    public ResponseEntity<ResResponse<List<ResCategoryDTO>>> getTopics(@RequestParam Map<String, String> params) {
        ResResponse<List<ResCategoryDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách chủ đề thành công");
        res.setData(this.topicService.getTopics(params));
        return ResponseEntity.ok(res);
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<ResResponse<ResCategoryDTO>> getTopicById(@PathVariable int id) {
        ResResponse<ResCategoryDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin chủ đề thành công");
        res.setData(this.topicService.getTopicById(id));
        return ResponseEntity.ok(res);
    }
}
