package com.paq.repository;

import com.paq.pojo.AnswerOption;
import java.util.List;

public interface AnswerOptionRepository {

    List<AnswerOption> getAnswersByQuestionId(int questionId);

    AnswerOption getAnswerById(int id);

    AnswerOption addOrUpdateAnswer(AnswerOption answer);

    void deleteAnswer(int id);
}
