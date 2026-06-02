/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.paq.controllers.client;

import com.paq.pojo.request.ReqChatMessageDTO;
import com.paq.pojo.response.ResChatMessageDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ChatMessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Admin
 */
@RestController
@RequestMapping("api/secure")
public class ApiChatMessageController {

    @Autowired
    private ChatMessageService messageService;

    @GetMapping("/chat-rooms/{roomId}/messages")
    public ResponseEntity<ResResponse<List<ResChatMessageDTO>>> getMessages(
            @PathVariable("roomId") int roomId,
            @RequestParam Map<String, String> params) {

        ResResponse<List<ResChatMessageDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách tin nhắn thành công");
        res.setData(this.messageService.getMessagesByRoomId(roomId, params));

        return ResponseEntity.ok(res);
    }

    @PostMapping("/chat-rooms/{roomId}/messages")
    public ResponseEntity<ResResponse<ResChatMessageDTO>> createMessage(
            @PathVariable("roomId") int roomId,
            @Valid @RequestBody ReqChatMessageDTO request) {

        ResResponse<ResChatMessageDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Gửi tin nhắn thành công");
        res.setData(this.messageService.createMessage(roomId, request));

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/chat-messages/{id}")
    public ResponseEntity<ResResponse<ResChatMessageDTO>> updateMessage(
            @PathVariable("id") int id,
            @Valid @RequestBody ReqChatMessageDTO request) {

        ResResponse<ResChatMessageDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật tin nhắn thành công");
        res.setData(this.messageService.updateMessage(id, request));

        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/chat-messages/{id}")
    public ResponseEntity<ResResponse<Object>> deleteMessage(@PathVariable("id") int id) {
        this.messageService.deleteMessage(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa tin nhắn thành công");

        return ResponseEntity.ok(res);
    }
}
