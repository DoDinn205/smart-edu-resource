/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.service;

import com.paq.pojo.request.ReqChatMessageDTO;
import com.paq.pojo.response.ResChatMessageDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Admin
 */
public interface ChatMessageService {

    List<ResChatMessageDTO> getMessagesByRoomId(int roomId, Map<String, String> params);

    ResChatMessageDTO createMessage(int roomId, ReqChatMessageDTO request);

    ResChatMessageDTO updateMessage(int id, ReqChatMessageDTO request);

    void deleteMessage(int id);
}
