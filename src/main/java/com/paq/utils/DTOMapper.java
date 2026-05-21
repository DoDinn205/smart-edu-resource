package com.paq.utils;

import com.paq.pojo.User;
import com.paq.pojo.response.ResUserDTO;

public class DTOMapper {

    public static ResUserDTO toResUserDTO(User user) {
        if (user == null) {
            return null;
        }

        ResUserDTO dto = new ResUserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAvatar(user.getAvatar());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole());

        return dto;
    }
}
