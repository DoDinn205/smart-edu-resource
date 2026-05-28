package com.paq.repository;

import com.paq.pojo.Enrollment;
import com.paq.pojo.QuizAttempt;
import com.paq.pojo.Student;
import java.util.List;
import java.util.Map;

public interface LearningResultRepository {

    List<QuizAttempt> getQuizAttempts(Map<String, String> params);

    QuizAttempt getQuizAttemptById(int id);

    List<Enrollment> getLearningProgressByCourseId(int courseId, Map<String, String> params);

    Student getStudentByUserId(int userId);
}
