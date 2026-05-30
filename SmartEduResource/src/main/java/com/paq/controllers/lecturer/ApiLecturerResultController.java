package com.paq.controllers.lecturer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paq.pojo.response.ResLearningProgressDTO;
import com.paq.pojo.response.ResQuizAttemptDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.LearningResultService;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerResultController {

    @Autowired
    private LearningResultService learningResultService;

    @GetMapping("/quiz-attempts")
    public ResponseEntity<ResResponse<List<ResQuizAttemptDTO>>> getQuizAttempts(
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResQuizAttemptDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách sinh viên làm bài thành công");
        res.setData(this.learningResultService.getQuizAttempts(params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/quiz-attempts/{id}")
    public ResponseEntity<ResResponse<ResQuizAttemptDTO>> getQuizAttemptById(@PathVariable int id) {
        ResResponse<ResQuizAttemptDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy chi tiết câu trả lời thành công");
        res.setData(this.learningResultService.getQuizAttemptById(id));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/courses/{courseId}/quiz-attempts")
    public ResponseEntity<ResResponse<List<ResQuizAttemptDTO>>> getQuizAttemptsByCourseId(
            @PathVariable int courseId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResQuizAttemptDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy kết quả quiz theo khóa học thành công");
        res.setData(this.learningResultService.getQuizAttemptsByCourseId(courseId, params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/quizzes/{quizId}/quiz-attempts")
    public ResponseEntity<ResResponse<List<ResQuizAttemptDTO>>> getQuizAttemptsByQuizId(
            @PathVariable int quizId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResQuizAttemptDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy điểm quiz thành công");
        res.setData(this.learningResultService.getQuizAttemptsByQuizId(quizId, params));

        return ResponseEntity.ok(res);
    }

    @GetMapping("/courses/{courseId}/progress")
    public ResponseEntity<ResResponse<List<ResLearningProgressDTO>>> getLearningProgressByCourseId(
            @PathVariable int courseId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResLearningProgressDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy tiến độ học tập thành công");
        res.setData(this.learningResultService.getLearningProgressByCourseId(courseId, params));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/progress/{enrollmentId}/feedback")
    public ResponseEntity<ResResponse<ResLearningProgressDTO>> updateLecturerFeedback(
            @PathVariable int enrollmentId,
            @RequestBody Map<String, String> body) {
        ResResponse<ResLearningProgressDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật đánh giá thành công");
        res.setData(this.learningResultService.updateLecturerFeedback(enrollmentId, body.get("feedback")));

        return ResponseEntity.ok(res);
    }
}
