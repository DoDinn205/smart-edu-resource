/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Enrollment;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface EnrollmentRepository {
    boolean existsByStudentAndCourse(int studentId, int courseId);
    Enrollment addEnrollment(Enrollment e);
    List<Enrollment> getEnrollmentsByStudentId(int studentId);
}
