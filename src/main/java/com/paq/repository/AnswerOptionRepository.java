package com.paq.repository;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.response.ResAnswerOptionDTO;
import java.util.List;

public interface AnswerOptionRepository {

    List<ResAnswerOptionDTO> getAnswersByQuestionId(int questionId);

    AnswerOption getAnswerById(int id);

    AnswerOption addOrUpdateAnswer(AnswerOption answer);

    void deleteAnswer(int id);
}
