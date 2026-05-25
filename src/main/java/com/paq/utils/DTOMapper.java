package com.paq.utils;

import com.paq.pojo.Course;
import com.paq.pojo.Enrollment;
import com.paq.pojo.AnswerOption;
import com.paq.pojo.Question;
import com.paq.pojo.Quiz;
import com.paq.pojo.Resource;
import com.paq.pojo.ResourceRelation;
import com.paq.pojo.ResourceTag;
import com.paq.pojo.ResourceType;
import com.paq.pojo.Student;
import com.paq.pojo.Subject;
import com.paq.pojo.Topic;
import com.paq.pojo.User;
import com.paq.pojo.response.ResCategoryDTO;
import com.paq.pojo.response.ResCourseDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.pojo.response.ResAnswerOptionDTO;
import com.paq.pojo.response.ResQuestionDTO;
import com.paq.pojo.response.ResQuizDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResSubjectDTO;
import com.paq.pojo.response.ResUserDTO;
import java.util.List;
import java.util.stream.Collectors;

public class DTOMapper {

    public static ResUserDTO toResUserDTO(User user) {
        if (user == null) {
            return null;
        }

        ResUserDTO dto = new ResUserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);

        return dto;
    }

    public static ResSubjectDTO toResSubjectDTO(Subject subject) {
        if (subject == null) {
            return null;
        }

        ResSubjectDTO dto = new ResSubjectDTO();
        dto.setId(subject.getId());
        dto.setCode(subject.getCode());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());
        dto.setCreatedAt(subject.getCreatedAt());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(Topic topic) {
        if (topic == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(topic.getId());
        dto.setName(topic.getName());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(ResourceType resourceType) {
        if (resourceType == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(resourceType.getId());
        dto.setName(resourceType.getName());

        return dto;
    }

    public static ResCategoryDTO toResCategoryDTO(ResourceTag resourceTag) {
        if (resourceTag == null) {
            return null;
        }

        ResCategoryDTO dto = new ResCategoryDTO();
        dto.setId(resourceTag.getId());
        dto.setName(resourceTag.getName());

        return dto;
    }

    public static ResCourseDTO toResCourseDTO(Course course) {
        if (course == null) {
            return null;
        }

        ResCourseDTO dto = new ResCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setStartDate(course.getStartDate());
        dto.setEndDate(course.getEndDate());
        dto.setIsPaid(course.getIsPaid());
        dto.setIsDeleted(course.getIsDeleted());
        dto.setTargetLevel(course.getTargetLevel() != null ? course.getTargetLevel().name() : null);

        if (course.getSubjectSet() != null) {
            List<ResSubjectDTO> subjects = course.getSubjectSet().stream()
                    .map(DTOMapper::toResSubjectDTO)
                    .collect(Collectors.toList());
            dto.setSubjects(subjects);
        }

        if (course.getEnrollmentSet() != null) {
            dto.setEnrollmentCount(course.getEnrollmentSet().size());
        }

        return dto;
    }

    public static ResResourceDTO toResResourceDTO(Resource resource) {
        if (resource == null) {
            return null;
        }

        ResResourceDTO dto = toResResourceBasicDTO(resource);
        dto.setDescription(resource.getDescription());
        dto.setThumbnailUrl(resource.getThumbnailUrl());
        dto.setFileSize(resource.getFileSize());
        dto.setCreatedAt(resource.getCreatedAt());
        dto.setUpdateAt(resource.getUpdateAt());
        dto.setPageCount(resource.getPageCount());
        dto.setIsDeleted(resource.getIsDeleted());
        dto.setUploadBy(toResUserDTO(resource.getUploadBy()));

        if (resource.getSubjectSet() != null) {
            dto.setSubjects(resource.getSubjectSet().stream()
                    .map(DTOMapper::toResSubjectDTO)
                    .collect(Collectors.toList()));
        }

        if (resource.getTopicSet() != null) {
            dto.setTopics(resource.getTopicSet().stream()
                    .map(DTOMapper::toResCategoryDTO)
                    .collect(Collectors.toList()));
        }

        if (resource.getResourceTagSet() != null) {
            dto.setTags(resource.getResourceTagSet().stream()
                    .map(DTOMapper::toResCategoryDTO)
                    .collect(Collectors.toList()));
        }

        if (resource.getResourceTypeSet() != null) {
            dto.setTypes(resource.getResourceTypeSet().stream()
                    .map(DTOMapper::toResCategoryDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ResResourceDTO toResResourceDTO(Resource resource, List<ResourceRelation> relations) {
        ResResourceDTO dto = toResResourceDTO(resource);
        if (dto != null && relations != null) {
            dto.setRelatedResources(relations.stream()
                    .map(ResourceRelation::getRelatedId)
                    .map(DTOMapper::toResResourceBasicDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    private static ResResourceDTO toResResourceBasicDTO(Resource resource) {
        if (resource == null) {
            return null;
        }

        ResResourceDTO dto = new ResResourceDTO();
        dto.setId(resource.getId());
        dto.setTitle(resource.getTitle());
        dto.setFileUrl(resource.getFileUrl());
        dto.setFormat(resource.getFormat() != null ? resource.getFormat().name() : null);
        dto.setLevel(resource.getLevel() != null ? resource.getLevel().name() : null);

        return dto;
    }

    public static ResEnrollmentDTO toResEnrollmentDTO(Enrollment enrollment) {
        if (enrollment == null) {
            return null;
        }

        ResEnrollmentDTO dto = new ResEnrollmentDTO();
        dto.setId(enrollment.getId());
        dto.setEnrollDate(enrollment.getEnrollDate());
        dto.setOverallProgress(enrollment.getOverallProgress());
        dto.setStatus(enrollment.getStatus() != null ? enrollment.getStatus().name() : null);
        dto.setTotalStudyTime(enrollment.getTotalStudyTime());
        dto.setCourseId(enrollment.getCourseId() != null ? enrollment.getCourseId().getId() : null);

        Student student = enrollment.getStudentId();
        if (student != null) {
            dto.setStudentId(student.getId());
            dto.setStudentCode(student.getStudentCode());
            dto.setUser(toResUserDTO(student.getUserId()));
        }

        return dto;
    }

    public static ResQuizDTO toResQuizDTO(Quiz quiz, boolean includeCorrectAnswers) {
        return toResQuizDTO(quiz, includeCorrectAnswers, true);
    }

    public static ResQuizDTO toResQuizDTO(Quiz quiz, boolean includeCorrectAnswers, boolean includeQuestions) {
        if (quiz == null) {
            return null;
        }

        ResQuizDTO dto = new ResQuizDTO();
        dto.setId(quiz.getId());
        dto.setTitle(quiz.getTitle());
        dto.setDescription(quiz.getDescription());
        dto.setDurationMinutes(quiz.getDurationMinutes());
        dto.setTotalScore(quiz.getTotalScore());
        dto.setCreatedAt(quiz.getCreatedAt());
        dto.setCourseId(quiz.getCourseId() != null ? quiz.getCourseId().getId() : null);
        dto.setCreatedBy(toResUserDTO(quiz.getCreatedBy()));

        if (includeQuestions && quiz.getQuestionSet() != null) {
            dto.setQuestions(quiz.getQuestionSet().stream()
                    .filter(q -> !Boolean.TRUE.equals(q.getIsDeleted()))
                    .map(q -> toResQuestionDTO(q, includeCorrectAnswers))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ResQuestionDTO toResQuestionDTO(Question question, boolean includeCorrectAnswers) {
        if (question == null) {
            return null;
        }

        ResQuestionDTO dto = new ResQuestionDTO();
        dto.setId(question.getId());
        dto.setContent(question.getContent());
        dto.setScore(question.getScore());
        if (includeCorrectAnswers) {
            dto.setExplanation(question.getExplanation());
        }
        dto.setType(question.getType() != null ? question.getType().name() : null);
        dto.setQuizId(question.getQuizId() != null ? question.getQuizId().getId() : null);

        if (question.getAnswerOptionSet() != null) {
            dto.setAnswers(question.getAnswerOptionSet().stream()
                    .filter(a -> !Boolean.TRUE.equals(a.getIsDeleted()))
                    .map(a -> toResAnswerOptionDTO(a, includeCorrectAnswers))
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static ResAnswerOptionDTO toResAnswerOptionDTO(AnswerOption answer, boolean includeCorrectAnswer) {
        if (answer == null) {
            return null;
        }

        ResAnswerOptionDTO dto = new ResAnswerOptionDTO();
        dto.setId(answer.getId());
        dto.setContent(answer.getContent());
        if (includeCorrectAnswer) {
            dto.setIsCorrect(answer.getIsCorrect());
        }

        return dto;
    }
}
