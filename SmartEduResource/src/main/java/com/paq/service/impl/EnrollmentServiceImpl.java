package com.paq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paq.pojo.Course;
import com.paq.pojo.Enrollment;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.EnrollmentRepository;
import com.paq.repository.UserRepository;
import com.paq.service.EnrollmentService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.EnrollmentStatusEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;

@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public List<ResEnrollmentDTO> getEnrollmentsByCourseId(int courseId, Map<String, String> params) {
        if (this.courseRepo.getCourseById(courseId) == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        return this.enrollmentRepo.getEnrollmentsByCourseId(courseId, params).stream()
                .map(DTOMapper::toResEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResEnrollmentDTO> getMyEnrollments(String username) {
        User user = this.resolveUser(username);
        Student student = this.userRepo.getStudentByUserId(user.getId());
        if (student == null) {
            throw new PermissionException("Tài khoản chưa có hồ sơ sinh viên");
        }

        return this.enrollmentRepo.getMyEnrollments(student.getId()).stream()
                .map(DTOMapper::toResEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResEnrollmentDTO enrollSelf(int courseId, String username) {
        User user = this.resolveUser(username);

        Course course = this.courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new IdInvalidException("Khóa học không tồn tại");
        }

        if (Boolean.TRUE.equals(course.getIsPaid())) {
            throw new IllegalArgumentException("Khóa học có phí — vui lòng thanh toán để đăng ký");
        }

        Student student = this.userRepo.getStudentByUserId(user.getId());
        if (student == null) {
            throw new PermissionException("Tài khoản chưa có hồ sơ sinh viên");
        }

        Enrollment existing = this.enrollmentRepo.findByCourseAndStudent(courseId, student.getId());
        if (existing != null) {
            return DTOMapper.toResEnrollmentDTO(existing);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setCourseId(course);
        enrollment.setStudentId(student);
        enrollment.setEnrollDate(new Date());
        enrollment.setStatus(EnrollmentStatusEnum.SUCCESS);
        enrollment.setOverallProgress(0.0);
        enrollment.setTotalStudyTime(0);

        return DTOMapper.toResEnrollmentDTO(this.enrollmentRepo.addOrUpdateEnrollment(enrollment));
    }

    @Override
    public ResEnrollmentDTO updateEnrollmentStatus(int id, ReqEnrollmentStatusDTO request) {
        Enrollment enrollment = this.enrollmentRepo.getEnrollmentById(id);
        if (enrollment == null) {
            throw new IdInvalidException("Enrollment không tồn tại");
        }

        enrollment.setStatus(request.getStatus());

        return DTOMapper.toResEnrollmentDTO(this.enrollmentRepo.addOrUpdateEnrollment(enrollment));
    }

    private User resolveUser(String username) {
        if (username == null) {
            throw new PermissionException("Bạn chưa đăng nhập");
        }
        User user = this.userRepo.getUserByUsername(username);
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            throw new PermissionException("Tài khoản không hợp lệ");
        }
        return user;
    }
}
