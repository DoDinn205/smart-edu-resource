package com.paq.utils;

import com.paq.pojo.Course;
import com.paq.pojo.Enrollment;
import com.paq.pojo.Interaction;
import com.paq.pojo.LearningLog;
import com.paq.pojo.Resource;
import com.paq.pojo.User;
import com.paq.pojo.response.ResCourseDTO;
import com.paq.pojo.response.ResEnrollmentDTO;
import com.paq.pojo.response.ResInteractionDTO;
import com.paq.pojo.response.ResLearningLogDTO;
import com.paq.pojo.response.ResResourceDTO;
import com.paq.pojo.response.ResUserDTO;
import java.text.SimpleDateFormat;

public class DTOMapper {

    private static final SimpleDateFormat DATE
            = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat DATETIME
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        dto.setRole(user.getRole());

        return dto;
    }

    public static ResCourseDTO toCourseDTO(Course c) {
        if (c == null) {
            return null;
        }

        ResCourseDTO dto = new ResCourseDTO();

        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setDescription(c.getDescription());

        dto.setStartDate(
                c.getStartDate() != null
                ? DATE.format(c.getStartDate()) : null
        );

        dto.setEndDate(
                c.getEndDate() != null
                ? DATE.format(c.getEndDate()) : null
        );

        dto.setIsPaid(c.getIsPaid());
        dto.setTargetLevel(c.getTargetLevel());

        return dto;
    }

    public static ResResourceDTO toResourceDTO(Resource r) {
        if (r == null) {
            return null;
        }

        ResResourceDTO dto = new ResResourceDTO();

        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setDescription(r.getDescription());

        return dto;
    }

    public static ResEnrollmentDTO toEnrollmentDTO(Enrollment e) {
        if (e == null) {
            return null;
        }

        ResEnrollmentDTO dto = new ResEnrollmentDTO();

        dto.setId(e.getId());
        dto.setStatus(e.getStatus());

        dto.setEnrollDate(
                e.getEnrollDate() != null
                ? DATETIME.format(e.getEnrollDate()) : null
        );

        dto.setOverallProgress(e.getOverallProgress());
        dto.setTotalStudyTime(e.getTotalStudyTime());

        if (e.getCourseId() != null) {
            dto.setCourse(toCourseDTO(e.getCourseId()));
        }

        return dto;
    }

    public static ResLearningLogDTO toLearningLogDTO(LearningLog l) {
        if (l == null) {
            return null;
        }

        ResLearningLogDTO dto = new ResLearningLogDTO();

        dto.setId(l.getId());

        dto.setStartTime(
                l.getStartTime() != null
                ? DATETIME.format(l.getStartTime()) : null
        );

        dto.setEndTime(
                l.getEndTime() != null
                ? DATETIME.format(l.getEndTime()) : null
        );

        dto.setCompletionStatus(l.getCompletionStatus());

        if (l.getResourceId() != null) {
            dto.setResourceId(l.getResourceId().getId());
            dto.setResourceTitle(l.getResourceId().getTitle());
        }

        if (l.getEnrollmentId() != null) {
            dto.setEnrollmentId(l.getEnrollmentId().getId());
        }

        return dto;
    }

    public static ResInteractionDTO toInteractionDTO(Interaction i) {

        ResInteractionDTO dto = new ResInteractionDTO();

        dto.setId(i.getId());
        dto.setNote(i.getNote());
        dto.setSelectedText(i.getSelectedText());
        dto.setType(i.getType());

        if (i.getCreatedAt() != null) {
            dto.setCreatedAt(DATETIME.format(i.getCreatedAt()));
        }

        if (i.getUpdatedAt() != null) {
            dto.setUpdatedAt(DATETIME.format(i.getUpdatedAt()));
        }

        dto.setPageNumber(i.getPageNumber());
        dto.setTimeOffsetSeconds(i.getTimeOffsetSeconds());
        dto.setPositionX(i.getPositionX());

        if (i.getResourceId() != null) {
            dto.setResourceId(i.getResourceId().getId());
            dto.setResourceTitle(i.getResourceId().getTitle());
        }

        if (i.getUserId() != null) {
            dto.setUserId(i.getUserId().getId());
            dto.setUsername(i.getUserId().getUsername());
            dto.setFullName(i.getUserId().getFullName());
        }

        return dto;
    }

    

}
