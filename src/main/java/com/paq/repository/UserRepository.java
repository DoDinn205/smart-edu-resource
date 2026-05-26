package com.paq.repository;

import com.paq.pojo.Lecturer;
import com.paq.pojo.User;

public interface UserRepository {

    User getUserByPhone(String phone);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    Lecturer getLecturerById(int id);

    Lecturer getLecturerByUserId(int userId);

    User addUser(User user);
}
