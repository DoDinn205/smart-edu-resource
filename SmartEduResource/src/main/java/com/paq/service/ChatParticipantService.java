package com.paq.service;

import com.paq.pojo.request.ReqChatParticipantDTO;
import com.paq.pojo.response.ResChatParticipantDTO;
import com.paq.pojo.response.ResPageDTO;
import java.util.List;
import java.util.Map;

public interface ChatParticipantService {

    ResPageDTO<ResChatParticipantDTO> getParticipantsByRoomId(int roomId, Map<String, String> params);

    ResChatParticipantDTO addParticipant(int roomId, ReqChatParticipantDTO request);

    void deleteParticipant(int id);
}
