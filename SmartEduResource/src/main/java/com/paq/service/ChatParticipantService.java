package com.paq.service;

import com.paq.pojo.request.ReqChatParticipantDTO;
import com.paq.pojo.response.ResChatParticipantDTO;
import java.util.List;
import java.util.Map;

public interface ChatParticipantService {

    List<ResChatParticipantDTO> getParticipantsByRoomId(int roomId, Map<String, String> params);

    ResChatParticipantDTO addParticipant(int roomId, ReqChatParticipantDTO request);

    void deleteParticipant(int id);
}
