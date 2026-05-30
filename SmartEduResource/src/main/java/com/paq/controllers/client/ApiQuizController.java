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

import com.paq.pojo.response.ResQuizDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.QuizService;

@RestController
@RequestMapping("/api")
public class ApiQuizController {

    @Autowired
    private QuizService quizService;

    @GetMapping("/quizzes")
    public ResponseEntity<ResResponse<List<ResQuizDTO>>> getQuizzes(@RequestParam Map<String, String> params) {
        ResResponse<List<ResQuizDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách quiz thành công");
        res.setData(this.quizService.getQuizzes(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/quizzes/{id}")
    public ResponseEntity<ResResponse<ResQuizDTO>> getQuizById(@PathVariable int id) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin quiz thành công");
        res.setData(this.quizService.getQuizById(id));

        return ResponseEntity.ok(res);
    }
}
