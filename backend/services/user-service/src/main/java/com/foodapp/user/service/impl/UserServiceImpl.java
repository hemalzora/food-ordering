package com.foodapp.user.service.impl;

import com.foodapp.user.dto.CreateUserRequest;
import com.foodapp.user.dto.UpdateUserRequest;
import com.foodapp.user.dto.UserDto;
import com.foodapp.user.entity.User;
import com.foodapp.user.exception.ResourceNotFoundException;
import com.foodapp.user.repository.UserRepository;
import com.foodapp.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserDto> listUsers() {
        return userRepository.findAll().stream().map(UserDto::from).toList();
    }

    @Override
    public UserDto getUser(Long userId) {
        return UserDto.from(requireUser(userId));
    }

    @Override
    @Transactional
    public UserDto registerUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest.name(), createUserRequest.email(), passwordEncoder.encode(createUserRequest.password()),
                createUserRequest.role(), createUserRequest.phone(), createUserRequest.gender(), createUserRequest.address());
        return UserDto.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        User user = requireUser(userId);
        // Managed entity: JPA dirty checking flushes these changes at commit - no save() needed.
        user.setName(updateUserRequest.name());
        user.setPhone(updateUserRequest.phone());
        user.setGender(updateUserRequest.gender());
        user.setAddress(updateUserRequest.address());
        return UserDto.from(user);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        requireUser(userId).setActive(false);
    }

    private User requireUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User " + userId + " not found"));
    }
}
