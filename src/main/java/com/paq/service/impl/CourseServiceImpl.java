package com.paq.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.Course;
import com.paq.pojo.Lecturer;
import com.paq.pojo.Subject;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqCourseDTO;
import com.paq.pojo.response.ResCourseDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.SubjectRepository;
import com.paq.repository.UserRepository;
import com.paq.service.CourseService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.RoleEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResCourseDTO> getCourses(Map<String, String> params) {
        return this.courseRepo.getCourses(params).stream()
                .map(DTOMapper::toResCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResCourseDTO getCourseById(int id) {
        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        return DTOMapper.toResCourseDTO(course);
    }

    @Override
    public ResCourseDTO createCourse(ReqCourseDTO request) {
        this.permissionService.requireLecturerOrAdmin();
        User currentUser = this.getCurrentUser();
        this.validateDates(request);

        if (this.courseRepo.getCourseByName(request.getName()) != null) {
            throw new IllegalArgumentException("Course name đã tồn tại");
        }

        Course course = new Course();
        course.setIsDeleted(Boolean.FALSE);
        course.setCreatedBy(currentUser);
        course.setLecturerId(this.resolveLecturerForCourse(currentUser, request.getLecturerId()));
        this.copyCourseFields(course, request);

        return DTOMapper.toResCourseDTO(this.courseRepo.addOrUpdateCourse(course));
    }

    @Override
    public ResCourseDTO updateCourse(int id, ReqCourseDTO request) {
        this.permissionService.requireCourseLecturerOrAdmin(id);
        User currentUser = this.getCurrentUser();
        this.validateDates(request);

        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }
        Course existedCourse = this.courseRepo.getCourseByName(request.getName());
        if (existedCourse != null && !existedCourse.getId().equals(id)) {
            throw new IllegalArgumentException("Course name đã tồn tại");
        }

        this.copyCourseFields(course, request);
        if (currentUser.getRole() == RoleEnum.ADMIN && request.getLecturerId() != null) {
            course.setLecturerId(this.resolveLecturerForCourse(currentUser, request.getLecturerId()));
        }

        return DTOMapper.toResCourseDTO(this.courseRepo.addOrUpdateCourse(course));
    }

    @Override
    public void deleteCourse(int id) {
        this.permissionService.requireCourseLecturerOrAdmin(id);
        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        this.courseRepo.deleteCourse(id);
    }

    private void validateDates(ReqCourseDTO request) {
        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getStartDate().after(request.getEndDate())) {
            throw new IllegalArgumentException("Start date phải trước hoặc bằng end date");
        }
    }

    private void copyCourseFields(Course course, ReqCourseDTO request) {
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setIsPaid(request.getIsPaid() != null ? request.getIsPaid() : Boolean.FALSE);
        course.setTargetLevel(request.getTargetLevel());
        course.setSubjectSet(this.resolveSubjects(request.getSubjectIds()));
    }

    private Set<Subject> resolveSubjects(Set<Integer> subjectIds) {
        Set<Subject> subjects = new HashSet<>();
        if (subjectIds == null || subjectIds.isEmpty()) {
            return subjects;
        }

        for (Integer subjectId : subjectIds) {
            Subject subject = this.subjectRepo.getSubjectById(subjectId);
            if (subject == null || Boolean.TRUE.equals(subject.getIsDeleted())) {
                throw new IdInvalidException("Subject không tồn tại: " + subjectId);
            }
            subjects.add(subject);
        }

        return subjects;
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

    private Lecturer resolveLecturerForCourse(User currentUser, Integer lecturerId) {
        if (currentUser.getRole() == RoleEnum.ADMIN) {
            if (lecturerId == null) {
                throw new IllegalArgumentException("Lecturer id là bắt buộc khi admin tạo hoặc cập nhật course");
            }

            Lecturer lecturer = this.userRepo.getLecturerById(lecturerId);
            if (lecturer == null) {
                throw new IdInvalidException("Lecturer không tồn tại");
            }

            return lecturer;
        }

        Lecturer lecturer = this.userRepo.getLecturerByUserId(currentUser.getId());
        if (lecturer == null) {
            throw new IdInvalidException("Tài khoản giảng viên chưa có hồ sơ giảng viên");
        }

        return lecturer;
    }

}
