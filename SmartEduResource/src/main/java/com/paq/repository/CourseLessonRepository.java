package com.paq.repository;

import com.paq.pojo.CourseLesson;
import java.util.List;

public interface CourseLessonRepository {

    List<CourseLesson> getLessonsByCourseId(int courseId);

    CourseLesson getLessonById(int id);

    void addLesson(CourseLesson lesson);

    void updateLesson(CourseLesson lesson);

    void deleteLesson(int id);

    boolean hasActiveEnrollment(int courseId, int studentId);

    boolean hasSuccessfulPayment(int courseId, int studentId);
}
