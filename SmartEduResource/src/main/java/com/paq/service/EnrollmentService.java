package com.paq.service;

import com.paq.pojo.request.ReqEnrollmentStatusDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import java.util.List;
import java.util.Map;

public interface EnrollmentService {

    List<ResEnrollmentDTO> getEnrollmentsByCourseId(int courseId, Map<String, String> params);

    ResEnrollmentDTO updateEnrollmentStatus(int id, ReqEnrollmentStatusDTO request);
}
