package com.paq.controllers.lecturer;

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

import com.paq.pojo.request.ReqChatParticipantDTO;
import com.paq.pojo.request.ReqChatRoomDTO;
import com.paq.pojo.request.ReqPrivateChatRoomDTO;
import com.paq.pojo.response.ResChatParticipantDTO;
import com.paq.pojo.response.ResChatRoomDTO;
import com.paq.pojo.response.ResPageDTO;
import com.paq.pojo.response.ResResponse;
import com.paq.service.ChatParticipantService;
import com.paq.service.ChatRoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/secure/lecturer")
public class ApiLecturerChatController {

    @Autowired
    private ChatRoomService roomService;

    @Autowired
    private ChatParticipantService participantService;

    @GetMapping("/chat-rooms")
    public ResponseEntity<ResResponse<ResPageDTO<ResChatRoomDTO>>> getRooms(
            @RequestParam Map<String, String> params) {
        ResResponse<ResPageDTO<ResChatRoomDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lấy danh sách phòng chat thành công");
        res.setData(this.roomService.getRooms(params));
        return ResponseEntity.ok(res);
    }

    @PostMapping("/chat-rooms")
    public ResponseEntity<ResResponse<ResChatRoomDTO>> createRoom(
            @Valid @RequestBody ReqChatRoomDTO request) {
        ResResponse<ResChatRoomDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo phòng chat thành công");
        res.setData(this.roomService.createRoom(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/chat-rooms/private")
    public ResponseEntity<ResResponse<ResChatRoomDTO>> createPrivateRoomWithLecturer(
            @Valid @RequestBody ReqPrivateChatRoomDTO request) {
        ResResponse<ResChatRoomDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Tạo phòng chat riêng với giảng viên thành công");
        res.setData(this.roomService.createPrivateRoomWithLecturer(request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PutMapping("/chat-rooms/{id}")
    public ResponseEntity<ResResponse<ResChatRoomDTO>> updateRoom(
            @PathVariable("id") int id,
            @Valid @RequestBody ReqChatRoomDTO request) {
        ResResponse<ResChatRoomDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Cập nhật phòng chat thành công");
        res.setData(this.roomService.updateRoom(id, request));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/chat-rooms/{id}")
    public ResponseEntity<ResResponse<Object>> deleteRoom(@PathVariable("id") int id) {
        this.roomService.deleteRoom(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa phòng chat thành công");
        return ResponseEntity.ok(res);
    }

    @PostMapping("/chat-rooms/{roomId}/participants")
    public ResponseEntity<ResResponse<ResChatParticipantDTO>> addParticipant(
            @PathVariable("roomId") int roomId,
            @Valid @RequestBody ReqChatParticipantDTO request) {
        ResResponse<ResChatParticipantDTO> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.CREATED.value());
        res.setMessage("Thêm người tham gia phòng chat thành công");
        res.setData(this.participantService.addParticipant(roomId, request));
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/chat-rooms/{roomId}/participants")
    public ResponseEntity<ResResponse<List<ResChatParticipantDTO>>> getParticipants(
            @PathVariable("roomId") int roomId,
            @RequestParam Map<String, String> params) {
        ResResponse<List<ResChatParticipantDTO>> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Lay danh sach nguoi tham gia phong chat thanh cong");
        res.setData(this.participantService.getParticipantsByRoomId(roomId, params));
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/chat-participants/{id}")
    public ResponseEntity<ResResponse<Object>> deleteParticipant(@PathVariable("id") int id) {
        this.participantService.deleteParticipant(id);

        ResResponse<Object> res = new ResResponse<>();
        res.setStatusCode(HttpStatus.OK.value());
        res.setMessage("Xóa người tham gia phòng chat thành công");
        return ResponseEntity.ok(res);
    }
}
