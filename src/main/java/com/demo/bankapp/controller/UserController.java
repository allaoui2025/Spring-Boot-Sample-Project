package com.demo.bankapp.controller;

import com.demo.bankapp.constant.Constants;
import com.demo.bankapp.dto.request.CreateUserRequest;
import com.demo.bankapp.dto.response.CreateUserResponse;
import com.demo.bankapp.exception.BadRequestException;
import com.demo.bankapp.model.User;
import com.demo.bankapp.service.UserService;
import com.demo.bankapp.service.WealthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final WealthService wealthService;

    @Autowired
    public UserController(UserService userService, WealthService wealthService) {
        this.userService = userService;
        this.wealthService = wealthService;
    }

    @PostMapping("/create")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest request) {

        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDUSERNAME);
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDPASSWORD);
        }

        if (request.getTcno() == null || request.getTcno().length() != 11 || !Pattern.matches("\\d{11}", request.getTcno())) {
            throw new BadRequestException(Constants.MESSAGE_INVALIDTCNO);
        }

        if (userService.isUsernameExist(request.getUsername())) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMEUSERNAMEEXIST);
        }

        if (userService.isTcnoExist(request.getTcno())) {
            throw new BadCredentialsException(Constants.MESSAGE_SAMETCNOEXIST);
        }

        User user = userService.createNewUser(new User(request.getUsername(), request.getPassword(), request.getTcno()));
        wealthService.newWealthRecord(user.getId());

        CreateUserResponse response = new CreateUserResponse();
        response.setUsername(user.getUsername());
        response.setTcno(user.getTcno());
        return response;
    }
}
