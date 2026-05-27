package com.paq.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.Enrollment;
import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.repository.CourseRepository;
import com.paq.repository.EnrollmentRepository;
import com.paq.service.EnrollmentService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Override
    public List<ResEnrollmentDTO> getEnrollmentsByCourseId(int courseId, Map<String, String> params) {
        if (this.courseRepo.getCourseById(courseId) == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        return this.enrollmentRepo.getEnrollmentsByCourseId(courseId, params).stream()
                .map(DTOMapper::toResEnrollmentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResEnrollmentDTO updateEnrollmentStatus(int id, ReqEnrollmentStatusDTO request) {
        Enrollment enrollment = this.enrollmentRepo.getEnrollmentById(id);
        if (enrollment == null) {
            throw new IdInvalidException("Enrollment không tồn tại");
        }

        enrollment.setStatus(request.getStatus());

        return DTOMapper.toResEnrollmentDTO(this.enrollmentRepo.addOrUpdateEnrollment(enrollment));
    }
}
