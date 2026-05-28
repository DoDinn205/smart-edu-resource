package com.paq.repository;

import com.paq.pojo.ChatRoom;
import java.util.List;
import java.util.Map;

public interface ChatRoomRepository {

    List<ChatRoom> getRooms(Map<String, String> params);

    ChatRoom getRoomById(int id);

    ChatRoom addOrUpdateRoom(ChatRoom room);

    void deleteRoom(int id);
}
