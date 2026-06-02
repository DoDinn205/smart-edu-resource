/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service.impl;

import com.paq.pojo.ChatMessage;
import com.paq.pojo.ChatRoom;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqChatMessageDTO;
import com.paq.pojo.response.ResChatMessageDTO;
import com.paq.repository.ChatMessageRepository;
import com.paq.repository.ChatRoomRepository;
import com.paq.service.ChatMessageService;
import com.paq.service.PermissionService;
import com.paq.utils.constant.RoleEnum;
import com.paq.utils.error.IdInvalidException;
import com.paq.utils.error.PermissionException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private static final SimpleDateFormat DATETIME_FORMAT
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private ChatMessageRepository messageRepo;

    @Autowired
    private ChatRoomRepository roomRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResChatMessageDTO> getMessagesByRoomId(int roomId, Map<String, String> params) {
        this.permissionService.requireChatRoomAccess(roomId);

        return this.messageRepo.getMessagesByRoomId(roomId, params)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResChatMessageDTO createMessage(int roomId, ReqChatMessageDTO request) {
        this.permissionService.requireChatRoomAccess(roomId);

        ChatRoom room = this.roomRepo.getRoomById(roomId);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        User user = this.permissionService.getCurrentUser();

        ChatMessage message = new ChatMessage();
        message.setContent(request.getContent());
        message.setRoomId(room);
        message.setSenderId(user);
        message.setSentAt(new Date());
        message.setIsDeleted(Boolean.FALSE);

        return this.toDTO(this.messageRepo.addOrUpdateMessage(message));
    }

    @Override
    public ResChatMessageDTO updateMessage(int id, ReqChatMessageDTO request) {
        ChatMessage message = this.messageRepo.getMessageById(id);
        if (message == null) {
            throw new IdInvalidException("Tin nhắn không tồn tại");
        }

        User user = this.permissionService.getCurrentUser();

        if (!message.getSenderId().getId().equals(user.getId())
                && user.getRole() != RoleEnum.ADMIN) {
            throw new PermissionException("Bạn không có quyền sửa tin nhắn này");
        }

        message.setContent(request.getContent());

        return this.toDTO(this.messageRepo.addOrUpdateMessage(message));
    }

    @Override
    public void deleteMessage(int id) {
        ChatMessage message = this.messageRepo.getMessageById(id);
        if (message == null) {
            throw new IdInvalidException("Tin nhắn không tồn tại");
        }

        User user = this.permissionService.getCurrentUser();

        if (!message.getSenderId().getId().equals(user.getId())
                && user.getRole() != RoleEnum.ADMIN) {
            throw new PermissionException("Bạn không có quyền xóa tin nhắn này");
        }

        this.messageRepo.deleteMessage(id);
    }

    private ResChatMessageDTO toDTO(ChatMessage m) {
        ResChatMessageDTO dto = new ResChatMessageDTO();

        dto.setId(m.getId());
        dto.setContent(m.getContent());

        if (m.getSentAt() != null) {
            dto.setSentAt(DATETIME_FORMAT.format(m.getSentAt()));
        }

        if (m.getRoomId() != null) {
            dto.setRoomId(m.getRoomId().getId());
        }

        if (m.getSenderId() != null) {
            dto.setSenderId(m.getSenderId().getId());
            dto.setSenderName(m.getSenderId().getFullName());
        }

        return dto;
    }

}
