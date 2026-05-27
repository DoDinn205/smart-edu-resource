package com.paq.service;

import com.paq.pojo.User;
import com.paq.pojo.request.ReqLecturerApprovalDTO;
import com.paq.pojo.request.ReqLecturerDTO;
import com.paq.pojo.request.ReqRegisterDTO;
import com.paq.pojo.request.ReqStudentDTO;
import com.paq.pojo.request.ReqUserStatusDTO;
import com.paq.pojo.response.ResLecturerDTO;
import com.paq.pojo.response.ResStudentDTO;
import com.paq.pojo.response.ResUserDTO;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    List<ResUserDTO> getUsers(Map<String, String> params);

    ResUserDTO getUserById(int id);

    User getUserByUsername(String username);

    User addUser(ReqRegisterDTO req);

    boolean authenticate(String username, String password);

    ResUserDTO updateUserStatus(int id, ReqUserStatusDTO request);

    List<ResStudentDTO> getStudents(Map<String, String> params);

    ResStudentDTO getStudentById(int id);

    ResStudentDTO createStudent(ReqStudentDTO request);

    ResStudentDTO updateStudent(int id, ReqStudentDTO request);

    void deleteStudent(int id);

    List<ResLecturerDTO> getLecturers(Map<String, String> params);

    ResLecturerDTO getLecturerById(int id);

    ResLecturerDTO createLecturer(ReqLecturerDTO request);

    ResLecturerDTO updateLecturer(int id, ReqLecturerDTO request);

    ResLecturerDTO updateLecturerApproval(int id, ReqLecturerApprovalDTO request);

    void deleteLecturer(int id);
}
