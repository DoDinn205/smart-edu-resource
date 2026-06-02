/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.repository;

import com.paq.pojo.ChatMessage;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface ChatMessageRepository {

    List<ChatMessage> getMessagesByRoomId(int roomId, Map<String, String> params);

    ChatMessage getMessageById(int id);

    ChatMessage addOrUpdateMessage(ChatMessage message);

    void deleteMessage(int id);
}
