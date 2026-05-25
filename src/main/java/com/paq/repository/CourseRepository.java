package com.paq.repository;

import com.paq.pojo.Course;
import java.util.List;
import java.util.Map;

public interface CourseRepository {

    List<Course> getCourses(Map<String, String> params);

    Course getCourseById(int id);

    Course getCourseByName(String name);

    Course addOrUpdateCourse(Course course);

    void deleteCourse(int id);
}
