package com.paq.repository;

import com.paq.pojo.ChatParticipant;
import java.util.List;
import java.util.Map;

public interface ChatParticipantRepository {

    List<ChatParticipant> getParticipantsByRoomId(int roomId, Map<String, String> params);

    ChatParticipant getParticipantById(int id);

    ChatParticipant getParticipantByRoomIdAndUserId(int roomId, int userId);

    ChatParticipant addParticipant(ChatParticipant participant);

    void deleteParticipant(int id);
}
