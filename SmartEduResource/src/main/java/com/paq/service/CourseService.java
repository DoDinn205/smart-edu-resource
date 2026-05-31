package com.paq.service;

import com.paq.pojo.request.ReqCourseDTO;
import com.paq.pojo.response.ResCourseDTO;
import java.util.List;
import java.util.Map;

public interface CourseService {

    List<ResCourseDTO> getCourses(Map<String, String> params);

    ResCourseDTO getCourseById(int id);

    ResCourseDTO createCourse(ReqCourseDTO request);

    ResCourseDTO updateCourse(int id, ReqCourseDTO request);

    void deleteCourse(int id);

    List<ResCourseDTO> getCoursesByCurrentLecturer(Map<String, String> params);

    Long countCoursesByCurrentLecturer(Map<String, String> params);
}
