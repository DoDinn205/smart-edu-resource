package com.paq.repository;

import com.paq.pojo.Quiz;
import java.util.List;
import java.util.Map;

public interface QuizRepository {

    List<Quiz> getQuizzes(Map<String, String> params);

    Quiz getQuizById(int id);

    Quiz addOrUpdateQuiz(Quiz quiz);

    void deleteQuiz(int id);
}
