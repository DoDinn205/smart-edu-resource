package com.paq.service;

import com.paq.pojo.request.ReqQuestionDTO;
import com.paq.pojo.response.ResQuestionDTO;
import java.util.List;

public interface QuestionService {

    List<ResQuestionDTO> getQuestionsByQuizId(int quizId);

    ResQuestionDTO createQuestion(int quizId, ReqQuestionDTO request);

    ResQuestionDTO updateQuestion(int id, ReqQuestionDTO request);

    void deleteQuestion(int id);
}
