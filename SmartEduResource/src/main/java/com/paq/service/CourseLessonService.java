package com.paq.service;

import com.paq.pojo.request.ReqCourseLessonDTO;
import com.paq.pojo.response.ResCourseLearnDTO;
import com.paq.pojo.response.ResCourseLessonDTO;
import java.util.List;

public interface CourseLessonService {

    /**
     * Get full learn page data.
     * Throws PermissionException if user has no access to a paid course.
     */
    ResCourseLearnDTO getLearnPage(int courseId, String username);

    List<ResCourseLessonDTO> getLessonsByCourseId(int courseId);

    ResCourseLessonDTO getLessonById(int id);

    ResCourseLessonDTO createLesson(ReqCourseLessonDTO request);

    ResCourseLessonDTO updateLesson(int id, ReqCourseLessonDTO request);

    void deleteLesson(int id);
}
