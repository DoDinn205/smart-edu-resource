package com.paq.repository;

import com.paq.pojo.User;

public interface UserRepository {

    User getUserByPhone(String phone);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User addUser(User user);
}
