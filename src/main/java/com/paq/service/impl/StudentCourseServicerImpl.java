/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.Course;
import com.paq.pojo.Enrollment;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import com.paq.repository.CourseRepository;
import com.paq.repository.EnrollmentRepository;
import com.paq.service.StudentCourseService;
import com.paq.service.UserService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StudentCourseServicerImpl implements StudentCourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<Course> getCourses() {
        return this.courseRepo.getCourses();
    }

    @Override
    public Course getCourseById(int id) {
        return this.courseRepo.getCourseById(id);
    }

    @Override
    public Enrollment enrollCourse(String username, int courseId) {
        User user = this.userService.getUserByUsername(username);

        if (user == null || user.getStudent() == null) {
            throw new RuntimeException("Tài khoản hiện tại không phải sinh viên!");
        }

        Student student = user.getStudent();
        Course course = this.courseRepo.getCourseById(courseId);

        if (course == null) {
            throw new RuntimeException("Không tìm thấy khóa học!");
        }

        if (this.enrollmentRepo.existsByStudentAndCourse(student.getId(), courseId)) {
            throw new RuntimeException("Bạn đã đăng ký khóa học này rồi!");
        }

        Enrollment e = new Enrollment();
        e.setStudentId(student);
        e.setCourseId(course);
        e.setEnrollDate(new Date());
        e.setOverallProgress(0.0);
        e.setTotalStudyTime(0);
        e.setStatus("ACTIVE");

        return this.enrollmentRepo.addEnrollment(e);
    }

    @Override
    public List<Enrollment> getMyCourses(String username) {
        User user = this.userService.getUserByUsername(username);

        if (user == null || user.getStudent() == null) {
            throw new RuntimeException("Tài khoản hiện tại không phải sinh viên!");
        }

        return this.enrollmentRepo.getEnrollmentsByStudentId(user.getStudent().getId());
    }

}
