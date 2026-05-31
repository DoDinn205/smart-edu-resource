/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.Student;
import com.paq.pojo.StudentAnswer;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqStudentAnswerDTO;
import com.paq.pojo.request.ReqSubmitQuizDTO;
import com.paq.pojo.response.ResQuizDTO;
import com.paq.pojo.response.ResQuizResultDTO;
import com.paq.repository.QuizRepository;
import com.paq.service.StudentQuizService;
import com.paq.service.UserService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.AttemptStatusEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StudentQuizServiceImpl implements StudentQuizService {

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private UserService userService;

    private final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ResQuizDTO> getQuizzes() {
        return this.quizRepo.getQuizzes(new HashMap<>())
                .stream()
                .map(q -> DTOMapper.toResQuizDTO(q, false, false))
                .collect(Collectors.toList());
    }

    @Override
    public ResQuizDTO getQuizById(int id) {
        Quiz quiz = this.quizRepo.getQuizById(id);

        if (quiz == null) {
            throw new IdInvalidException("Quiz khong ton tai");
        }

        return DTOMapper.toResQuizDTO(quiz, false, true);
    }

    @Override
    public ResQuizResultDTO submitQuiz(String username, int quizId, ReqSubmitQuizDTO request) {
        User user = this.userService.getUserByUsername(username);

        if (user == null || user.getStudent() == null) {
            throw new PermissionException("Tai khoan hien tai khong phai sinh vien");
        }

        Quiz quiz = this.quizRepo.getQuizById(quizId);

        if (quiz == null) {
            throw new IdInvalidException("Quiz khong ton tai");
        }

        Student student = user.getStudent();

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuizId(quiz);
        attempt.setStudentId(student);
        attempt.setStartedAt(new Date());
        attempt.setSubmittedAt(new Date());
        attempt.setStatus(AttemptStatusEnum.SUBMITTED);

        double totalScore = 0;
        int correctCount = 0;
        int totalQuestions = request.getAnswers().size();

        QuizAttempt savedAttempt = this.quizRepo.addQuizAttempt(attempt);

        for (ReqStudentAnswerDTO ans : request.getAnswers()) {
            Question question = this.findQuestionInQuiz(quizId, ans.getQuestionId());

            if (question == null) {
                throw new IdInvalidException("Question khong ton tai trong quiz");
            }

            AnswerOption selectedOption = null;
            boolean isCorrect = false;
            double answerScore = 0;

            if (ans.getOptionId() != null) {
                selectedOption = this.quizRepo.getAnswerOptionById(ans.getOptionId());

                if (selectedOption == null
                        || selectedOption.getQuestionId() == null
                        || !selectedOption.getQuestionId().getId().equals(question.getId())) {
                    throw new IdInvalidException("Dap an khong hop le");
                }

                isCorrect = Boolean.TRUE.equals(selectedOption.getIsCorrect());
                if (isCorrect) {
                    answerScore = question.getScore() != null ? question.getScore() : 0;
                    correctCount++;
                    totalScore += answerScore;
                }
            }

            StudentAnswer studentAnswer = new StudentAnswer();
            studentAnswer.setAttemptId(savedAttempt);
            studentAnswer.setQuestionId(question);
            studentAnswer.setOptionId(selectedOption);
            studentAnswer.setAnswerText(ans.getAnswerText());
            studentAnswer.setIsCorrect(isCorrect);
            studentAnswer.setScore(answerScore);

            this.quizRepo.addStudentAnswer(studentAnswer);
        }

        savedAttempt.setScore(totalScore);

        return this.toQuizResultDTO(savedAttempt, totalQuestions, correctCount);
    }

    @Override
    public List<ResQuizResultDTO> getMyQuizResults(String username) {
        return this.quizRepo.getAttemptsByUsername(username)
                .stream()
                .map(a -> this.toQuizResultDTO(a, null, null))
                .collect(Collectors.toList());
    }

    private Question findQuestionInQuiz(int quizId, int questionId) {
        return this.quizRepo.getQuestionsByQuizId(quizId)
                .stream()
                .filter(q -> q.getId().equals(questionId))
                .findFirst()
                .orElse(null);
    }

    private ResQuizResultDTO toQuizResultDTO(
            QuizAttempt attempt,
            Integer totalQuestions,
            Integer correctAnswers) {

        ResQuizResultDTO dto = new ResQuizResultDTO();

        dto.setAttemptId(attempt.getId());

        if (attempt.getQuizId() != null) {
            dto.setQuizId(attempt.getQuizId().getId());
            dto.setQuizTitle(attempt.getQuizId().getTitle());
        }

        dto.setScore(attempt.getScore());
        dto.setStatus(attempt.getStatus() != null ? attempt.getStatus().name() : null);

        dto.setSubmittedAt(
                attempt.getSubmittedAt() != null
                ? datetimeFormat.format(attempt.getSubmittedAt()) : null
        );

        if (totalQuestions != null) {
            dto.setTotalQuestions(totalQuestions);
        }

        if (correctAnswers != null) {
            dto.setCorrectAnswers(correctAnswers);
            dto.setWrongAnswers(totalQuestions - correctAnswers);
        }

        return dto;
    }

}
