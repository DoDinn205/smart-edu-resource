/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.Interaction;
import com.paq.pojo.Resource;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqInteractionDTO;
import com.paq.pojo.response.ResInteractionDTO;
import com.paq.repository.InteractionRepository;
import com.paq.repository.ResourceRepository;
import com.paq.service.StudentInteractionService;
import com.paq.service.UserService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class StudentInteractionServiceImpl implements StudentInteractionService {

    @Autowired
    private InteractionRepository interactionRepo;

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<ResInteractionDTO> getInteractionsByResourceId(int resourceId) {
        return this.interactionRepo.getInteractionsByResourceId(resourceId)
                .stream()
                .map(i -> DTOMapper.toInteractionDTO(i))
                .collect(Collectors.toList());
    }

    @Override
    public ResInteractionDTO createInteraction(String username, int resourceId, ReqInteractionDTO request) {
        User user = this.userService.getUserByUsername(username);
        Resource resource = this.resourceRepo.getResourceById(resourceId);

        if (user == null) {
            throw new PermissionException("User không hợp lệ");
        }

        if (resource == null) {
            throw new IdInvalidException("Resource không tồn tại");
        }

        if (request.getNote() == null || request.getNote().trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung không được để trống");
        }

        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Loại interaction không được để trống");
        }

        Interaction interaction = new Interaction();
        interaction.setNote(request.getNote());
        interaction.setType(request.getType());
        interaction.setSelectedText(request.getSelectedText());
        interaction.setPageNumber(request.getPageNumber());
        interaction.setTimeOffsetSeconds(request.getTimeOffsetSeconds());
        interaction.setPositionX(request.getPositionX());
        interaction.setCreatedAt(new Date());
        interaction.setUpdatedAt(new Date());
        interaction.setResourceId(resource);
        interaction.setUserId(user);

        return DTOMapper.toInteractionDTO(
                this.interactionRepo.addInteraction(interaction)
        );
    }

    @Override
    public ResInteractionDTO updateInteraction(String username, int interactionId, ReqInteractionDTO request) {
        User user = this.userService.getUserByUsername(username);
        Interaction interaction = this.interactionRepo.getInteractionById(interactionId);

        if (interaction == null) {
            throw new IdInvalidException("Interaction không tồn tại");
        }

        if (user == null || interaction.getUserId() == null
                || !interaction.getUserId().getId().equals(user.getId())) {
            throw new PermissionException("Bạn chỉ được sửa nội dung của chính mình");
        }

        if (request.getNote() == null || request.getNote().trim().isEmpty()) {
            throw new IllegalArgumentException("Nội dung không được để trống");
        }

        interaction.setNote(request.getNote());
        interaction.setSelectedText(request.getSelectedText());
        interaction.setPageNumber(request.getPageNumber());
        interaction.setTimeOffsetSeconds(request.getTimeOffsetSeconds());
        interaction.setPositionX(request.getPositionX());
        interaction.setUpdatedAt(new Date());

        if (request.getType() != null && !request.getType().trim().isEmpty()) {
            interaction.setType(request.getType());
        }

        return DTOMapper.toInteractionDTO(
                this.interactionRepo.updateInteraction(interaction)
        );
    }

    @Override
    public ResInteractionDTO deleteInteraction(String username, int interactionId) {
        User user = this.userService.getUserByUsername(username);
        Interaction interaction = this.interactionRepo.getInteractionById(interactionId);

        if (interaction == null) {
            throw new IdInvalidException("Interaction không tồn tại");
        }

        if (user == null || interaction.getUserId() == null
                || !interaction.getUserId().getId().equals(user.getId())) {
            throw new PermissionException("Bạn chỉ được xóa nội dung của chính mình");
        }
        interaction.setNote("[Đã xóa]");
        interaction.setUpdatedAt(new Date());

        return DTOMapper.toInteractionDTO(
                this.interactionRepo.updateInteraction(interaction)
        );
    }

}
