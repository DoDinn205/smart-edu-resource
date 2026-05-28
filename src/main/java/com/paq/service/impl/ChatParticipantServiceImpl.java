package com.paq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ChatParticipant;
import com.paq.pojo.ChatRoom;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqChatParticipantDTO;
import com.paq.pojo.response.ResChatParticipantDTO;
import com.paq.repository.ChatParticipantRepository;
import com.paq.repository.ChatRoomRepository;
import com.paq.repository.UserRepository;
import com.paq.service.ChatParticipantService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class ChatParticipantServiceImpl implements ChatParticipantService {

    @Autowired
    private ChatParticipantRepository participantRepo;

    @Autowired
    private ChatRoomRepository roomRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResChatParticipantDTO> getParticipantsByRoomId(int roomId, Map<String, String> params) {
        this.permissionService.requireChatRoomAccess(roomId);

        ChatRoom room = this.roomRepo.getRoomById(roomId);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        return this.participantRepo.getParticipantsByRoomId(roomId, params).stream()
                .map(DTOMapper::toResChatParticipantDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResChatParticipantDTO addParticipant(int roomId, ReqChatParticipantDTO request) {
        this.permissionService.requireChatRoomManager(roomId);

        ChatRoom room = this.roomRepo.getRoomById(roomId);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        User user = this.userRepo.getUserById(request.getUserId());
        if (user == null || Boolean.FALSE.equals(user.getIsActive())) {
            throw new IdInvalidException("User không tồn tại");
        }

        ChatParticipant existed = this.participantRepo.getParticipantByRoomIdAndUserId(roomId, user.getId());
        if (existed != null) {
            throw new IllegalArgumentException("User đã tham gia phòng chat này");
        }

        ChatParticipant participant = new ChatParticipant();
        participant.setRoomId(room);
        participant.setUserId(user);
        participant.setJoinedAt(new Date());
        participant.setIsMuted(Boolean.FALSE);

        return DTOMapper.toResChatParticipantDTO(this.participantRepo.addParticipant(participant));
    }

    @Override
    public void deleteParticipant(int id) {
        ChatParticipant participant = this.participantRepo.getParticipantById(id);
        if (participant == null) {
            throw new IdInvalidException("Chat participant không tồn tại");
        }

        this.permissionService.requireChatRoomManager(participant.getRoomId().getId());
        this.participantRepo.deleteParticipant(id);
    }
}
