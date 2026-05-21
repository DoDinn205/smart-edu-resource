package com.paq.service;

import com.paq.pojo.User;
import com.paq.pojo.request.ReqRegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getUserByUsername(String username);

    User addUser(ReqRegisterDTO req);

    boolean authenticate(String username, String password);
}
