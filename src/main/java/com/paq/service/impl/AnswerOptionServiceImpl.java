package com.paq.service.impl;

import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.request.ReqAnswerOptionDTO;
import com.paq.pojo.response.ResAnswerOptionDTO;
import com.paq.repository.AnswerOptionRepository;
import com.paq.repository.QuestionRepository;
import com.paq.service.AnswerOptionService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.constant.QuestionTypeEnum;
import com.paq.utils.error.IdInvalidException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerOptionServiceImpl implements AnswerOptionService {

    @Autowired
    private AnswerOptionRepository answerRepo;

    @Autowired
    private QuestionRepository questionRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResAnswerOptionDTO> getAnswersByQuestionId(int questionId) {
        Question question = this.getExistingQuestion(questionId);
        this.permissionService.requireQuizOwnerOrAdmin(question.getQuizId().getId());

        return this.answerRepo.getAnswersByQuestionId(questionId).stream()
                .map(a -> DTOMapper.toResAnswerOptionDTO(a, true))
                .collect(Collectors.toList());
    }

    @Override
    public ResAnswerOptionDTO createAnswer(int questionId, ReqAnswerOptionDTO request) {
        Question question = this.getExistingQuestion(questionId);
        this.permissionService.requireQuizOwnerOrAdmin(question.getQuizId().getId());

        AnswerOption answer = new AnswerOption();
        answer.setIsDeleted(Boolean.FALSE);
        answer.setQuestionId(question);
        this.copyAnswerFields(answer, request);
        this.keepSingleCorrectAnswer(question, answer);

        return DTOMapper.toResAnswerOptionDTO(this.answerRepo.addOrUpdateAnswer(answer), true);
    }

    @Override
    public ResAnswerOptionDTO updateAnswer(int id, ReqAnswerOptionDTO request) {
        AnswerOption answer = this.getExistingAnswer(id);
        Question question = answer.getQuestionId();
        this.permissionService.requireQuizOwnerOrAdmin(question.getQuizId().getId());

        this.copyAnswerFields(answer, request);
        this.keepSingleCorrectAnswer(question, answer);

        return DTOMapper.toResAnswerOptionDTO(this.answerRepo.addOrUpdateAnswer(answer), true);
    }

    @Override
    public void deleteAnswer(int id) {
        AnswerOption answer = this.getExistingAnswer(id);
        this.permissionService.requireQuizOwnerOrAdmin(answer.getQuestionId().getQuizId().getId());

        this.answerRepo.deleteAnswer(id);
    }

    private Question getExistingQuestion(int questionId) {
        Question question = this.questionRepo.getQuestionById(questionId);
        if (question == null) {
            throw new IdInvalidException("Question khong ton tai");
        }

        return question;
    }

    private AnswerOption getExistingAnswer(int id) {
        AnswerOption answer = this.answerRepo.getAnswerById(id);
        if (answer == null) {
            throw new IdInvalidException("Answer khong ton tai");
        }

        return answer;
    }

    private void copyAnswerFields(AnswerOption answer, ReqAnswerOptionDTO request) {
        answer.setContent(request.getContent());
        answer.setIsCorrect(request.getIsCorrect());
    }

    private void keepSingleCorrectAnswer(Question question, AnswerOption currentAnswer) {
        if (!Boolean.TRUE.equals(currentAnswer.getIsCorrect())) {
            return;
        }

        if (question.getType() != QuestionTypeEnum.SINGLE_CHOICE
                && question.getType() != QuestionTypeEnum.TRUE_FALSE) {
            return;
        }

        for (AnswerOption answer : this.answerRepo.getAnswersByQuestionId(question.getId())) {
            if (currentAnswer.getId() == null || !currentAnswer.getId().equals(answer.getId())) {
                answer.setIsCorrect(Boolean.FALSE);
                this.answerRepo.addOrUpdateAnswer(answer);
            }
        }
    }
}
