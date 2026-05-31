package com.paq.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.paq.pojo.Enrollment;
import com.paq.pojo.Quiz;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import com.paq.pojo.response.ResLearningProgressDTO;
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResQuizAttemptDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.EnrollmentRepository;
import com.paq.repository.LearningResultRepository;
import com.paq.repository.QuizRepository;
import com.paq.repository.UserRepository;
import com.paq.service.LearningResultService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.RoleEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;

@Service
public class LearningResultServiceImpl implements LearningResultService {

    @Autowired
    private LearningResultRepository learningResultRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private Environment env;

    @Override
    public List<ResQuizAttemptDTO> getQuizAttempts(Map<String, String> params) {
        Map<String, String> safeParams = this.applyCurrentUserScope(params);
        this.requireLecturerOrAdminWhenReadingOtherStudent(safeParams);

        return this.learningResultRepo.getQuizAttempts(safeParams).stream()
                .map(qa -> DTOMapper.toResQuizAttemptDTO(qa, false))
                .collect(Collectors.toList());
    }

    @Override
    public ResQuizAttemptDTO getQuizAttemptById(int id) {
        QuizAttempt attempt = this.getExistingAttempt(id);
        this.requireAttemptAccess(attempt);

        return DTOMapper.toResQuizAttemptDTO(attempt, true);
    }

    @Override
    public List<ResQuizAttemptDTO> getQuizAttemptsByCourseId(int courseId, Map<String, String> params) {
        if (this.courseRepo.getCourseById(courseId) == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        Map<String, String> safeParams = this.copyParams(params);
        safeParams.put("courseId", String.valueOf(courseId));
        return this.getQuizAttempts(safeParams);
    }

    @Override
    public List<ResQuizAttemptDTO> getQuizAttemptsByQuizId(int quizId, Map<String, String> params) {
        Quiz quiz = this.quizRepo.getQuizById(quizId);
        if (quiz == null) {
            throw new IdInvalidException("Quiz không tồn tại");
        }

        Map<String, String> safeParams = this.copyParams(params);
        safeParams.put("quizId", String.valueOf(quizId));
        return this.getQuizAttempts(safeParams);
    }

    @Override
    public ResPageDTO<ResLearningProgressDTO> getLearningProgress(Map<String, String> params) {
        Map<String, String> safeParams = this.copyParams(params);
        User user = this.getCurrentUser();
        if (user.getRole() == RoleEnum.STUDENT) {
            safeParams.put("studentId", String.valueOf(this.getCurrentStudent().getId()));
        } else {
            this.permissionService.requireLecturerOrAdmin();
            if (user.getRole() == RoleEnum.LECTURER) {
                safeParams.put("lecturerUserId", String.valueOf(user.getId()));
            }
        }

        int page = safeParams.containsKey("page") ? Integer.parseInt(safeParams.get("page")) : 1;
        int pageSize = this.env.getProperty("enrollments.page_size", Integer.class, 10);
        Long totalItems = this.learningResultRepo.countLearningProgress(safeParams);
        List<ResLearningProgressDTO> items = this.learningResultRepo.getLearningProgress(safeParams).stream()
                .map(e -> this.toProgressDTO(e, e.getCourseId().getId()))
                .collect(Collectors.toList());

        return DTOMapper.toResPageDTO(items, totalItems, page, pageSize);
    }

    @Override
    public ResPageDTO<ResLearningProgressDTO> getLearningProgressByCourseId(int courseId, Map<String, String> params) {
        if (this.courseRepo.getCourseById(courseId) == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        Map<String, String> safeParams = this.copyParams(params);
        User user = this.getCurrentUser();
        if (user.getRole() == RoleEnum.STUDENT) {
            safeParams.put("studentId", String.valueOf(this.getCurrentStudent().getId()));
        } else {
            this.permissionService.requireCourseLecturerOrAdmin(courseId);
        }

        int page = safeParams.containsKey("page") ? Integer.parseInt(safeParams.get("page")) : 1;
        int pageSize = this.env.getProperty("enrollments.page_size", Integer.class, 10);
        Long totalItems = this.learningResultRepo.countLearningProgressByCourseId(courseId, safeParams);
        List<ResLearningProgressDTO> items = this.learningResultRepo.getLearningProgressByCourseId(courseId, safeParams).stream()
                .map(e -> this.toProgressDTO(e, courseId))
                .collect(Collectors.toList());

        return DTOMapper.toResPageDTO(items, totalItems, page, pageSize);
    }

    @Override
    public ResLearningProgressDTO updateLecturerFeedback(int enrollmentId, String feedback) {
        Enrollment enrollment = this.enrollmentRepo.getEnrollmentById(enrollmentId);
        if (enrollment == null) {
            throw new IdInvalidException("Không tìm thấy kết quả học tập");
        }

        this.permissionService.requireCourseLecturerOrAdmin(enrollment.getCourseId().getId());

        enrollment.setLecturerFeedback(feedback);
        Enrollment savedEnrollment = this.enrollmentRepo.addOrUpdateEnrollment(enrollment);
        
        return this.toProgressDTO(savedEnrollment, savedEnrollment.getCourseId().getId());
    }

    private ResLearningProgressDTO toProgressDTO(Enrollment enrollment, int courseId) {
        Map<String, String> attemptParams = new HashMap<>();
        attemptParams.put("courseId", String.valueOf(courseId));
        attemptParams.put("studentId", String.valueOf(enrollment.getStudentId().getId()));
        List<ResQuizAttemptDTO> quizAttempts = this.learningResultRepo.getQuizAttempts(attemptParams).stream()
                .map(qa -> DTOMapper.toResQuizAttemptDTO(qa, false))
                .collect(Collectors.toList());

        return DTOMapper.toResLearningProgressDTO(enrollment, quizAttempts);
    }

    private QuizAttempt getExistingAttempt(int id) {
        QuizAttempt attempt = this.learningResultRepo.getQuizAttemptById(id);
        if (attempt == null) {
            throw new IdInvalidException("Quiz attempt không tồn tại");
        }

        return attempt;
    }

    private void requireAttemptAccess(QuizAttempt attempt) {
        User user = this.getCurrentUser();
        if (user.getRole() == RoleEnum.ADMIN) {
            return;
        }

        if (user.getRole() == RoleEnum.STUDENT
                && attempt.getStudentId() != null
                && attempt.getStudentId().getUserId() != null
                && user.getId().equals(attempt.getStudentId().getUserId().getId())) {
            return;
        }

        if (user.getRole() == RoleEnum.LECTURER && attempt.getQuizId() != null) {
            this.permissionService.requireCourseLecturerOrAdmin(attempt.getQuizId().getCourseId().getId());
            return;
        }

        throw new PermissionException("Bạn không có quyền xem kết quả này");
    }

    private Map<String, String> applyCurrentUserScope(Map<String, String> params) {
        Map<String, String> safeParams = this.copyParams(params);
        User user = this.getCurrentUser();
        if (user.getRole() == RoleEnum.STUDENT) {
            safeParams.put("studentId", String.valueOf(this.getCurrentStudent().getId()));
        }

        return safeParams;
    }

    private void requireLecturerOrAdminWhenReadingOtherStudent(Map<String, String> params) {
        User user = this.getCurrentUser();
        if (user.getRole() == RoleEnum.STUDENT) {
            return;
        }

        this.permissionService.requireLecturerOrAdmin();
    }

    private Student getCurrentStudent() {
        User user = this.getCurrentUser();
        Student student = this.learningResultRepo.getStudentByUserId(user.getId());
        if (student == null) {
            throw new PermissionException("Tài khoản sinh viên không hợp lệ");
        }

        return student;
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

    private Map<String, String> copyParams(Map<String, String> params) {
        Map<String, String> copied = new HashMap<>();
        if (params != null) {
            copied.putAll(params);
        }

        return copied;
    }
}
