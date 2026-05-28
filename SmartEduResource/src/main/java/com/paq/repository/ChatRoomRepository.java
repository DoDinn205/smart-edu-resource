package com.paq.repository;

import com.paq.pojo.ChatRoom;
import java.util.List;
import java.util.Map;

public interface ChatRoomRepository {

    List<ChatRoom> getRooms(Map<String, String> params);

    List<ChatRoom> getRoomsAvailableToUser(Map<String, String> params, int userId);

    ChatRoom getRoomById(int id);

    ChatRoom getPrivateRoomByCourseAndUsers(int courseId, int firstUserId, int secondUserId);

    ChatRoom addOrUpdateRoom(ChatRoom room);

    void deleteRoom(int id);
}
