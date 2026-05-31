package com.paq.controllers.lecturer;

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

import com.paq.pojo.request.ReqAnswerOptionDTO;
import com.paq.pojo.request.ReqQuestionDTO;
import com.paq.pojo.request.ReqQuizDTO;
import com.paq.pojo.response.ResAnswerOptionDTO;
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResQuestionDTO;
import com.paq.pojo.response.ResQuizDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.AnswerOptionService;
import com.paq.service.QuestionService;
import com.paq.service.QuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerQuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerOptionService answerOptionService;

    @Autowired
    private Environment env;

    @GetMapping("/quizzes")
    public ResponseEntity<ResResponse<ResPageDTO<ResQuizDTO>>> getLecturerQuizzes(@RequestParam Map<String, String> params) {
        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        int pageSize = this.env.getProperty("quizzes.page_size", Integer.class);

        Map<String, String> countParams = new HashMap<>(params);
        Long totalItems = this.quizService.countLecturerQuizzes(countParams);

        ResPageDTO<ResQuizDTO> pageDTO = com.paq.utils.DTOMapper.toResPageDTO(this.quizService.getLecturerQuizzes(params), totalItems, page, pageSize);

        ResResponse<ResPageDTO<ResQuizDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách bài tập thành công");
        res.setData(pageDTO);

        return ResponseEntity.ok(res);
    }

    @GetMapping("/quizzes/{id}/manage")
    public ResponseEntity<ResResponse<ResQuizDTO>> getQuizForManagement(@PathVariable("id") int id) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy thông tin quản lý quiz thành công");
        res.setData(this.quizService.getQuizForManagement(id));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/quizzes")
    public ResponseEntity<ResResponse<ResQuizDTO>> createQuiz(@Valid @RequestBody ReqQuizDTO request) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo quiz thành công");
        res.setData(this.quizService.createQuiz(request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/quizzes/{id}")
    public ResponseEntity<ResResponse<ResQuizDTO>> updateQuiz(@PathVariable("id") int id,
            @Valid @RequestBody ReqQuizDTO request) {
        ResResponse<ResQuizDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật quiz thành công");
        res.setData(this.quizService.updateQuiz(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/quizzes/{id}")
    public ResponseEntity<ResResponse<Object>> deleteQuiz(@PathVariable("id") int id) {
        this.quizService.deleteQuiz(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa quiz thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<ResResponse<List<ResQuestionDTO>>> getQuestionsByQuizId(@PathVariable("quizId") int quizId) {
        ResResponse<List<ResQuestionDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách câu hỏi thành công");
        res.setData(this.questionService.getQuestionsByQuizId(quizId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    public ResponseEntity<ResResponse<ResQuestionDTO>> createQuestion(@PathVariable("quizId") int quizId,
            @Valid @RequestBody ReqQuestionDTO request) {
        ResResponse<ResQuestionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo câu hỏi thành công");
        res.setData(this.questionService.createQuestion(quizId, request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<ResResponse<ResQuestionDTO>> updateQuestion(@PathVariable("id") int id,
            @Valid @RequestBody ReqQuestionDTO request) {
        ResResponse<ResQuestionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật câu hỏi thành công");
        res.setData(this.questionService.updateQuestion(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<ResResponse<Object>> deleteQuestion(@PathVariable("id") int id) {
        this.questionService.deleteQuestion(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa câu hỏi thành công");

        return ResponseEntity.ok(res);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<ResResponse<List<ResAnswerOptionDTO>>> getAnswersByQuestionId(
            @PathVariable("questionId") int questionId) {
        ResResponse<List<ResAnswerOptionDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách đáp án thành công");
        res.setData(this.answerOptionService.getAnswersByQuestionId(questionId));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<ResResponse<ResAnswerOptionDTO>> createAnswer(@PathVariable("questionId") int questionId,
            @Valid @RequestBody ReqAnswerOptionDTO request) {
        ResResponse<ResAnswerOptionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo đáp án thành công");
        res.setData(this.answerOptionService.createAnswer(questionId, request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/answers/{id}")
    public ResponseEntity<ResResponse<ResAnswerOptionDTO>> updateAnswer(@PathVariable("id") int id,
            @Valid @RequestBody ReqAnswerOptionDTO request) {
        ResResponse<ResAnswerOptionDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật đáp án thành công");
        res.setData(this.answerOptionService.updateAnswer(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<ResResponse<Object>> deleteAnswer(@PathVariable("id") int id) {
        this.answerOptionService.deleteAnswer(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa đáp án thành công");

        return ResponseEntity.ok(res);
    }
}
