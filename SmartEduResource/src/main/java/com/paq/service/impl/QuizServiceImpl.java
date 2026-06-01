package com.paq.service.impl;

import com.paq.pojo.Course;
import com.paq.pojo.Lecturer;
import com.paq.pojo.Quiz;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqQuizDTO;
import com.paq.pojo.response.ResQuizDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.QuizRepository;
import com.paq.repository.UserRepository;
import com.paq.service.PermissionService;
import com.paq.service.NotificationPublisherService;
import com.paq.service.QuizService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private NotificationPublisherService notificationPublisher;

    @Override
    public List<ResQuizDTO> getQuizzes(Map<String, String> params) {
        return this.quizRepo.getQuizzes(params).stream()
                .map(q -> DTOMapper.toResQuizDTO(q, false, false))
                .collect(Collectors.toList());
    }

    @Override
    public List<ResQuizDTO> getLecturerQuizzes(Map<String, String> params) {
        this.permissionService.requireLecturerOrAdmin();
        params = this.applyLecturerFilters(params);
        if (params.containsKey("courseId")) {
            this.permissionService.requireCourseLecturerOrAdmin(Integer.parseInt(params.get("courseId")));
        }
        
        return this.quizRepo.getQuizzes(params).stream()
                .map(q -> DTOMapper.toResQuizDTO(q, true, false))
                .collect(Collectors.toList());
    }

    @Override
    public Long countLecturerQuizzes(Map<String, String> params) {
        this.permissionService.requireLecturerOrAdmin();
        params = this.applyLecturerFilters(params);

        return this.quizRepo.countQuizzes(params);
    }

    private Map<String, String> applyLecturerFilters(Map<String, String> params) {
        User currentUser = this.getCurrentUser();
        Lecturer lecturer = this.userRepo.getLecturerByUserId(currentUser.getId());
        if (lecturer == null) {
            throw new IdInvalidException("Tài khoản giảng viên chưa có hồ sơ giảng viên");
        }

        if (params == null) {
            params = new HashMap<>();
        }
        params.put("lecturerId", String.valueOf(lecturer.getId()));

        return params;
    }

    @Override
    public ResQuizDTO getQuizById(int id) {
        Quiz quiz = this.getExistingQuiz(id);
        return DTOMapper.toResQuizDTO(quiz, false);
    }

    @Override
    public ResQuizDTO getQuizForManagement(int id) {
        this.permissionService.requireQuizOwnerOrAdmin(id);
        return DTOMapper.toResQuizDTO(this.getExistingQuiz(id), true);
    }

    @Override
    public ResQuizDTO createQuiz(ReqQuizDTO request) {
        this.permissionService.requireLecturerOrAdmin();
        this.permissionService.requireCourseLecturerOrAdmin(request.getCourseId());

        Course course = this.courseRepo.getCourseById(request.getCourseId());
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        Quiz quiz = new Quiz();
        quiz.setIsDeleted(Boolean.FALSE);
        quiz.setCreatedAt(new Date());
        quiz.setCreatedBy(this.getCurrentUser());
        quiz.setCourseId(course);
        quiz.setTotalScore(0D);
        this.copyQuizFields(quiz, request);

        Quiz savedQuiz = this.quizRepo.addOrUpdateQuiz(quiz);
        this.notificationPublisher.notifyCourseStudents(
                course.getId(),
                "Khóa học có bài kiểm tra mới",
                "Bài kiểm tra \"" + savedQuiz.getTitle() + "\" vừa được thêm vào khóa học " + course.getName() + ".");
        return DTOMapper.toResQuizDTO(savedQuiz, true);
    }

    @Override
    public ResQuizDTO updateQuiz(int id, ReqQuizDTO request) {
        this.permissionService.requireQuizOwnerOrAdmin(id);
        this.permissionService.requireCourseLecturerOrAdmin(request.getCourseId());

        Quiz quiz = this.getExistingQuiz(id);
        Course course = this.courseRepo.getCourseById(request.getCourseId());
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        quiz.setCourseId(course);
        this.copyQuizFields(quiz, request);

        return DTOMapper.toResQuizDTO(this.quizRepo.addOrUpdateQuiz(quiz), true);
    }

    @Override
    public void deleteQuiz(int id) {
        this.permissionService.requireQuizOwnerOrAdmin(id);
        this.quizRepo.deleteQuiz(id);
    }

    private Quiz getExistingQuiz(int id) {
        Quiz quiz = this.quizRepo.getQuizById(id);
        if (quiz == null) {
            throw new IdInvalidException("Quiz không tồn tại");
        }

        return quiz;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new PermissionException("Bạn chưa đăng nhập");
        }

        User user = this.userRepo.getUserByUsername(auth.getName());
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            throw new PermissionException("Tài khoản không hợp lệ");
        }

        return user;
    }

    private void copyQuizFields(Quiz quiz, ReqQuizDTO request) {
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDurationMinutes(request.getDurationMinutes());
    }
}
