package com.paq.service;

import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import java.util.List;
import java.util.Map;

public interface EnrollmentService {

    List<ResEnrollmentDTO> getEnrollmentsByCourseId(int courseId, Map<String, String> params);

    List<ResEnrollmentDTO> getMyEnrollments(String username);

    ResEnrollmentDTO enrollSelf(int courseId, String username);

    ResEnrollmentDTO updateEnrollmentStatus(int id, ReqEnrollmentStatusDTO request);
}
