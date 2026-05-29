package com.paq.repository;

import com.paq.pojo.Enrollment;
import java.util.List;
import java.util.Map;

public interface EnrollmentRepository {

    List<Enrollment> getEnrollmentsByCourseId(int courseId, Map<String, String> params);

    List<Enrollment> getMyEnrollments(int studentId);

    Enrollment findByCourseAndStudent(int courseId, int studentId);

    Enrollment getEnrollmentById(int id);

    Enrollment addOrUpdateEnrollment(Enrollment enrollment);

    boolean existsByCourseId(int courseId);

    boolean existsByCourseIdAndUserId(int courseId, int userId);
}
