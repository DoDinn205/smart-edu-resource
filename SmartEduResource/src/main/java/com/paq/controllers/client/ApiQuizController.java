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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.request.ReqAnswerOptionDTO;
import com.paq.pojo.request.ReqQuestionDTO;
import com.paq.pojo.request.ReqQuizDTO;
import com.paq.pojo.response.ResAnswerOptionDTO;
import com.paq.pojo.response.ResQuestionDTO;
import com.paq.pojo.response.ResQuizDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.AnswerOptionService;
import com.paq.service.QuestionService;
import com.paq.service.QuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ApiQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerOptionService answerOptionService;

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

    @GetMapping("/secure/quizzes/{id}")
    public ResponseEntity<ResResponse<ResQuizDTO>> getQuizForManagement(@PathVariable int id) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin quản lý quiz thành công");
        res.setData(this.quizService.getQuizForManagement(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/quizzes")
    public ResponseEntity<ResResponse<ResQuizDTO>> createQuiz(@Valid @RequestBody ReqQuizDTO request) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo quiz thành công");
        res.setData(this.quizService.createQuiz(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/quizzes/{id}")
    public ResponseEntity<ResResponse<ResQuizDTO>> updateQuiz(@PathVariable int id,
            @Valid @RequestBody ReqQuizDTO request) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật quiz thành công");
        res.setData(this.quizService.updateQuiz(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/quizzes/{id}")
    public ResponseEntity<ResResponse<Object>> deleteQuiz(@PathVariable int id) {
        this.quizService.deleteQuiz(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa quiz thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/quizzes/{quizId}/questions")
    public ResponseEntity<ResResponse<List<ResQuestionDTO>>> getQuestionsByQuizId(@PathVariable int quizId) {
        ResResponse<List<ResQuestionDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách câu hỏi thành công");
        res.setData(this.questionService.getQuestionsByQuizId(quizId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/quizzes/{quizId}/questions")
    public ResponseEntity<ResResponse<ResQuestionDTO>> createQuestion(@PathVariable int quizId,
            @Valid @RequestBody ReqQuestionDTO request) {
        ResResponse<ResQuestionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo câu hỏi thành công");
        res.setData(this.questionService.createQuestion(quizId, request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/questions/{id}")
    public ResponseEntity<ResResponse<ResQuestionDTO>> updateQuestion(@PathVariable int id,
            @Valid @RequestBody ReqQuestionDTO request) {
        ResResponse<ResQuestionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật câu hỏi thành công");
        res.setData(this.questionService.updateQuestion(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/questions/{id}")
    public ResponseEntity<ResResponse<Object>> deleteQuestion(@PathVariable int id) {
        this.questionService.deleteQuestion(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa câu hỏi thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/secure/questions/{questionId}/answers")
    public ResponseEntity<ResResponse<List<ResAnswerOptionDTO>>> getAnswersByQuestionId(@PathVariable int questionId) {
        ResResponse<List<ResAnswerOptionDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách đáp án thành công");
        res.setData(this.answerOptionService.getAnswersByQuestionId(questionId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/secure/questions/{questionId}/answers")
    public ResponseEntity<ResResponse<ResAnswerOptionDTO>> createAnswer(@PathVariable int questionId,
            @Valid @RequestBody ReqAnswerOptionDTO request) {
        ResResponse<ResAnswerOptionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo đáp án thành công");
        res.setData(this.answerOptionService.createAnswer(questionId, request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/secure/answers/{id}")
    public ResponseEntity<ResResponse<ResAnswerOptionDTO>> updateAnswer(@PathVariable int id,
            @Valid @RequestBody ReqAnswerOptionDTO request) {
        ResResponse<ResAnswerOptionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật đáp án thành công");
        res.setData(this.answerOptionService.updateAnswer(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/secure/answers/{id}")
    public ResponseEntity<ResResponse<Object>> deleteAnswer(@PathVariable int id) {
        this.answerOptionService.deleteAnswer(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa đáp án thành công");

        return ResponseEntity.ok(res);
    }
}
