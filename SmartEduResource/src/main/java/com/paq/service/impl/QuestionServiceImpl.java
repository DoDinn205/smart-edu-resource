package com.paq.service.impl;

import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.AnswerOption;
import com.paq.pojo.request.ReqQuestionDTO;
import com.paq.pojo.request.ReqAnswerOptionDTO;
import com.paq.pojo.response.ResQuestionDTO;
import com.paq.repository.QuestionRepository;
import com.paq.repository.QuizRepository;
import com.paq.service.PermissionService;
import com.paq.service.QuestionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResQuestionDTO> getQuestionsByQuizId(int quizId) {
        this.permissionService.requireQuizOwnerOrAdmin(quizId);
        return this.questionRepo.getQuestionsByQuizId(quizId).stream()
                .map(q -> DTOMapper.toResQuestionDTO(q, true))
                .collect(Collectors.toList());
    }

    @Override
    public ResQuestionDTO createQuestion(int quizId, ReqQuestionDTO request) {
        this.permissionService.requireQuizOwnerOrAdmin(quizId);
        Quiz quiz = this.getExistingQuiz(quizId);

        Question question = new Question();
        question.setIsDeleted(Boolean.FALSE);
        question.setQuizId(quiz);
        this.copyQuestionFields(question, request);

        Question savedQuestion = this.questionRepo.addOrUpdateQuestion(question);
        this.refreshQuizTotalScore(quizId);

        return DTOMapper.toResQuestionDTO(savedQuestion, true);
    }

    @Override
    public ResQuestionDTO updateQuestion(int id, ReqQuestionDTO request) {
        Question question = this.getExistingQuestion(id);
        this.permissionService.requireQuizOwnerOrAdmin(question.getQuizId().getId());

        this.copyQuestionFields(question, request);
        Question savedQuestion = this.questionRepo.addOrUpdateQuestion(question);
        this.refreshQuizTotalScore(question.getQuizId().getId());

        return DTOMapper.toResQuestionDTO(savedQuestion, true);
    }

    @Override
    public void deleteQuestion(int id) {
        Question question = this.getExistingQuestion(id);
        this.permissionService.requireQuizOwnerOrAdmin(question.getQuizId().getId());

        this.questionRepo.deleteQuestion(id);
        this.refreshQuizTotalScore(question.getQuizId().getId());
    }

    private Quiz getExistingQuiz(int quizId) {
        Quiz quiz = this.quizRepo.getQuizById(quizId);
        if (quiz == null) {
            throw new IdInvalidException("Quiz khong ton tai");
        }

        return quiz;
    }

    private Question getExistingQuestion(int id) {
        Question question = this.questionRepo.getQuestionById(id);
        if (question == null) {
            throw new IdInvalidException("Question khong ton tai");
        }

        return question;
    }

    private void copyQuestionFields(Question question, ReqQuestionDTO request) {
        question.setContent(request.getContent());
        question.setScore(request.getScore());
        question.setExplanation(request.getExplanation());
        question.setType(request.getType());

        if (request.getOptions() != null) {
            if (question.getAnswerOptionSet() == null) {
                question.setAnswerOptionSet(new HashSet<>());
            } else {
                question.getAnswerOptionSet().clear();
            }

            for (ReqAnswerOptionDTO opt : request.getOptions()) {
                AnswerOption answerOption = new AnswerOption();
                answerOption.setContent(opt.getContent());
                answerOption.setIsCorrect(opt.getIsCorrect());
                answerOption.setIsDeleted(false);
                answerOption.setQuestionId(question);
                question.getAnswerOptionSet().add(answerOption);
            }
        }
    }

    private void refreshQuizTotalScore(int quizId) {
        Quiz quiz = this.getExistingQuiz(quizId);
        double totalScore = this.questionRepo.getQuestionsByQuizId(quizId).stream()
                .map(Question::getScore)
                .filter(score -> score != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        quiz.setTotalScore(totalScore);
        this.quizRepo.addOrUpdateQuiz(quiz);
    }
}
