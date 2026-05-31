/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Enrollment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface EnrollmentRepository {

    boolean existsByStudentAndCourse(int studentId, int courseId);

    boolean existsByCourseIdAndUserId(int courseId, int userId);

    Enrollment addOrUpdateEnrollment(Enrollment enrollment);

    Enrollment getEnrollmentById(int id);

    Enrollment addEnrollment(Enrollment enrollment);

    Enrollment findByCourseAndStudent(int courseId, int studentId);

    List<Enrollment> getEnrollmentsByStudentId(int studentId);

    List<Enrollment> getEnrollmentsByUsername(String username);

    List<Enrollment> getEnrollmentsByCourseId(int courseId, Map<String, String> params);

    Long countEnrollmentsByCourseId(int courseId, Map<String, String> params);

    List<Enrollment> getMyEnrollments(int studentId);
}
