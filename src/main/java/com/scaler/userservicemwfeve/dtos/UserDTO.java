package com.scaler.userservicemwfeve.dtos;

import com.scaler.userservicemwfeve.models.Role;
import com.scaler.userservicemwfeve.models.User;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class UserDTO {
    private String name;
    private String email;
    private String hashedPassword;
    @ManyToMany
    private List<Role> roles;
    private boolean isEmailVerified;

    public static UserDTO from(User user) {
        if(user==null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.name = user.getName();
        userDTO.email = user.getEmail();
        userDTO.hashedPassword = user.getHashedPassword();
        userDTO.roles = user.getRoles();
        userDTO.isEmailVerified = user.isEmailVerified();

        return userDTO;
    }
}
