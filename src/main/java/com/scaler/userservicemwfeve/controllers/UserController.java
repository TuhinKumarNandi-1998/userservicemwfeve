package com.scaler.userservicemwfeve.controllers;

import com.scaler.userservicemwfeve.Exceptions.IncorrectPasswordException;
import com.scaler.userservicemwfeve.Exceptions.TokenNotExistException;
import com.scaler.userservicemwfeve.Exceptions.UserNotFoundException;
import com.scaler.userservicemwfeve.Services.UserService;
import com.scaler.userservicemwfeve.dtos.LoginRequestDto;
import com.scaler.userservicemwfeve.dtos.LogoutRequestDto;
import com.scaler.userservicemwfeve.dtos.SignUpRequestDto;
import com.scaler.userservicemwfeve.dtos.UserDTO;
import com.scaler.userservicemwfeve.models.Token;
import com.scaler.userservicemwfeve.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto loginRequestDto) throws UserNotFoundException, IncorrectPasswordException {
        // check if email and password in db
        Token token = userService.login(loginRequestDto.getEmail(),
                loginRequestDto.getPassword());
        // if yes return user
        // else throw some error
        return token;
    }

    @PostMapping("/signup")
    public UserDTO signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
        // no need to hash password for now
        // just store user as is in the db
        // for now no need to have email verification either

        User user = userService.signup(signUpRequestDto.getEmail(),
                signUpRequestDto.getPassword(),
                signUpRequestDto.getName());
        return UserDTO.from(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto logoutRequestDto) throws TokenNotExistException {
        // delete token if exists -> 200
        // if doesn't exist give a 404

        userService.logout(logoutRequestDto.getToken());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validate/{token}")
    public UserDTO validateToken(@PathVariable("token") String token) {
        return UserDTO.from(userService.validateToken(token));
    }
}
