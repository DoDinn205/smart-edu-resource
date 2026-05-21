/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.Course;
import com.paq.pojo.Enrollment;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface StudentCourseService {

    List<Course> getCourses();

    Course getCourseById(int id);

    Enrollment enrollCourse(String username, int courseId);

    List<Enrollment> getMyCourses(String username);
}
