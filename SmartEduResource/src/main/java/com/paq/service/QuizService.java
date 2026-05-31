package com.paq.service;

import com.paq.pojo.request.ReqQuizDTO;
import com.paq.pojo.response.ResQuizDTO;
import java.util.List;
import java.util.Map;

public interface QuizService {

    List<ResQuizDTO> getQuizzes(Map<String, String> params);

    List<ResQuizDTO> getLecturerQuizzes(Map<String, String> params);

    Long countLecturerQuizzes(Map<String, String> params);

    ResQuizDTO getQuizById(int id);

    ResQuizDTO getQuizForManagement(int id);

    ResQuizDTO createQuiz(ReqQuizDTO request);

    ResQuizDTO updateQuiz(int id, ReqQuizDTO request);

    void deleteQuiz(int id);
}
