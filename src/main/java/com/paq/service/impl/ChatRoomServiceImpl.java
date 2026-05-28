package com.paq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paq.pojo.ChatRoom;
import com.paq.pojo.Course;
import com.paq.pojo.User;
import com.paq.pojo.request.ReqChatRoomDTO;
import com.paq.pojo.response.ResChatRoomDTO;
import com.paq.repository.ChatRoomRepository;
import com.paq.repository.CourseRepository;
import com.paq.service.ChatRoomService;
import com.paq.service.PermissionService;
import com.paq.utils.DTOMapper;
import com.paq.utils.error.IdInvalidException;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

    @Autowired
    private ChatRoomRepository roomRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Autowired
    private PermissionService permissionService;

    @Override
    public List<ResChatRoomDTO> getRooms(Map<String, String> params) {
        this.permissionService.requireLecturerOrAdmin();
        return this.roomRepo.getRooms(params).stream()
                .map(DTOMapper::toResChatRoomDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResChatRoomDTO getRoomById(int id) {
        this.permissionService.requireChatRoomAccess(id);

        ChatRoom room = this.roomRepo.getRoomById(id);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        return DTOMapper.toResChatRoomDTO(room);
    }

    @Override
    public ResChatRoomDTO createRoom(ReqChatRoomDTO request) {
        User user = this.permissionService.getCurrentUser();
        Course course = this.resolveCourse(request.getCourseId());
        if (course != null) {
            this.permissionService.requireCourseLecturerOrAdmin(course.getId());
        } else {
            this.permissionService.requireLecturerOrAdmin();
        }

        ChatRoom room = new ChatRoom();
        room.setType(request.getType());
        room.setName(request.getName());
        room.setCourseId(course);
        room.setCreatedBy(user);
        room.setCreatedAt(new Date());

        return DTOMapper.toResChatRoomDTO(this.roomRepo.addOrUpdateRoom(room));
    }

    @Override
    public ResChatRoomDTO updateRoom(int id, ReqChatRoomDTO request) {
        this.permissionService.requireChatRoomManager(id);

        ChatRoom room = this.roomRepo.getRoomById(id);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        Course course = this.resolveCourse(request.getCourseId());
        if (course != null) {
            this.permissionService.requireCourseLecturerOrAdmin(course.getId());
        }

        room.setType(request.getType());
        room.setName(request.getName());
        room.setCourseId(course);

        return DTOMapper.toResChatRoomDTO(this.roomRepo.addOrUpdateRoom(room));
    }

    @Override
    public void deleteRoom(int id) {
        this.permissionService.requireChatRoomManager(id);

        ChatRoom room = this.roomRepo.getRoomById(id);
        if (room == null) {
            throw new IdInvalidException("Chat room không tồn tại");
        }

        this.roomRepo.deleteRoom(id);
    }

    private Course resolveCourse(Integer courseId) {
        if (courseId == null) {
            return null;
        }

        Course course = this.courseRepo.getCourseById(courseId);
        if (course == null) {
            throw new IdInvalidException("Course không tồn tại");
        }

        return course;
    }
}
