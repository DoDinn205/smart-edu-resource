package com.paq.repository;

import com.paq.pojo.Question;
import java.util.List;

public interface QuestionRepository {

    List<Question> getQuestionsByQuizId(int quizId);

    Question getQuestionById(int id);

    Question addOrUpdateQuestion(Question question);

    void deleteQuestion(int id);
}
