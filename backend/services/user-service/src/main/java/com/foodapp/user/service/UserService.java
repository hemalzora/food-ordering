package com.foodapp.user.service;

import com.foodapp.user.dto.CreateUserRequest;
import com.foodapp.user.dto.UpdateUserRequest;
import com.foodapp.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> listUsers();

    UserDto getUser(Long userId);

    UserDto registerUser(CreateUserRequest createUserRequest);

    UserDto updateUser(Long userId, UpdateUserRequest updateUserRequest);

    void deactivateUser(Long userId);
}
