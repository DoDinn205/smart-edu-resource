package com.paq.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.Course;
import com.paq.pojo.Subject;
import com.paq.pojo.request.ReqCourseDTO;
import com.paq.pojo.response.ResCourseDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.SubjectRepository;
import com.paq.service.CourseService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private SubjectRepository subjectRepo;

    @Override
    public List<ResCourseDTO> getCourses(Map<String, String> params) {
        return this.courseRepo.getCourses(params).stream()
                .map(DTOMapper::toResCourseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResCourseDTO getCourseById(int id) {
        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        return DTOMapper.toResCourseDTO(course);
    }

    @Override
    public ResCourseDTO createCourse(ReqCourseDTO request) {
        this.validateDates(request);

        if (this.courseRepo.getCourseByName(request.getName()) != null) {
            throw new IllegalArgumentException("Course name đã tồn tại");
        }

        Course course = new Course();
        course.setIsDeleted(Boolean.FALSE);
        this.copyCourseFields(course, request);

        return DTOMapper.toResCourseDTO(this.courseRepo.addOrUpdateCourse(course));
    }

    @Override
    public ResCourseDTO updateCourse(int id, ReqCourseDTO request) {
        this.validateDates(request);

        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        Course existedCourse = this.courseRepo.getCourseByName(request.getName());
        if (existedCourse != null && !existedCourse.getId().equals(id)) {
            throw new IllegalArgumentException("Course name đã tồn tại");
        }

        this.copyCourseFields(course, request);

        return DTOMapper.toResCourseDTO(this.courseRepo.addOrUpdateCourse(course));
    }

    @Override
    public void deleteCourse(int id) {
        Course course = this.courseRepo.getCourseById(id);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        this.courseRepo.deleteCourse(id);
    }

    private void validateDates(ReqCourseDTO request) {
        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getStartDate().after(request.getEndDate())) {
            throw new IllegalArgumentException("Start date phải trước hoặc bằng end date");
        }
    }

    private void copyCourseFields(Course course, ReqCourseDTO request) {
        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setStartDate(request.getStartDate());
        course.setEndDate(request.getEndDate());
        course.setIsPaid(request.getIsPaid() != null ? request.getIsPaid() : Boolean.FALSE);
        course.setTargetLevel(request.getTargetLevel());
        course.setSubjectSet(this.resolveSubjects(request.getSubjectIds()));
    }

    private Set<Subject> resolveSubjects(Set<Integer> subjectIds) {
        Set<Subject> subjects = new HashSet<>();
        if (subjectIds == null || subjectIds.isEmpty()) {
            return subjects;
        }

        for (Integer subjectId : subjectIds) {
            Subject subject = this.subjectRepo.getSubjectById(subjectId);
            if (subject == null || Boolean.TRUE.equals(subject.getIsDeleted())) {
                throw new IdInvalidException("Subject không tồn tại: " + subjectId);
            }
            subjects.add(subject);
        }

        return subjects;
    }
}
