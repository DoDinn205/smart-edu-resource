package com.paq.repository;

import com.paq.pojo.Lecturer;
import com.paq.pojo.Student;
import com.paq.pojo.User;
import java.util.List;
import java.util.Map;

public interface UserRepository {

    List<User> getUsers(Map<String, String> params);

    User getUserById(int id);

    User getUserByPhone(String phone);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    List<Student> getStudents(Map<String, String> params);

    Student getStudentById(int id);

    Student getStudentByUserId(int userId);

    Student addOrUpdateStudent(Student student);

    List<Lecturer> getLecturers(Map<String, String> params);

    Lecturer getLecturerById(int id);

    Lecturer getLecturerByUserId(int userId);

    Lecturer addOrUpdateLecturer(Lecturer lecturer);

    User addUser(User user);

    User updateUser(User user);
}
