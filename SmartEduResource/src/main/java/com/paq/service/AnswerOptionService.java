package com.paq.service;

import com.paq.pojo.request.ReqAnswerOptionDTO;
import com.paq.pojo.response.ResAnswerOptionDTO;
import java.util.List;

public interface AnswerOptionService {

    List<ResAnswerOptionDTO> getAnswersByQuestionId(int questionId);

    ResAnswerOptionDTO createAnswer(int questionId, ReqAnswerOptionDTO request);

    ResAnswerOptionDTO updateAnswer(int id, ReqAnswerOptionDTO request);

    void deleteAnswer(int id);
}
