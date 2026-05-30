package com.paq.service;

import com.paq.pojo.response.ResLearningProgressDTO;
import com.paq.pojo.response.ResQuizAttemptDTO;
import java.util.List;
import java.util.Map;

public interface LearningResultService {

    List<ResQuizAttemptDTO> getQuizAttempts(Map<String, String> params);

    ResQuizAttemptDTO getQuizAttemptById(int id);

    List<ResQuizAttemptDTO> getQuizAttemptsByCourseId(int courseId, Map<String, String> params);

    List<ResQuizAttemptDTO> getQuizAttemptsByQuizId(int quizId, Map<String, String> params);

    List<ResLearningProgressDTO> getLearningProgressByCourseId(int courseId, Map<String, String> params);

    ResLearningProgressDTO updateLecturerFeedback(int enrollmentId, String feedback);
}
