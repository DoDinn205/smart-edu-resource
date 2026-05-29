/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.Course;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface CourseRepository {

    List<Course> getCourses(Map<String, String> params);

    Course getCourseById(int id);

    Course getCourseByName(String name);

    Course addOrUpdateCourse(Course course);

    void deleteCourse(int id);
}
