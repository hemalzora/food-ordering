package com.foodapp.user.controller;

import com.foodapp.user.dto.CreateUserRequest;
import com.foodapp.user.dto.UpdateUserRequest;
import com.foodapp.user.dto.UserDto;
import com.foodapp.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users", description = "Register and manage user accounts")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "List users", description = "Returns all users.")
    @ApiResponse(responseCode = "200", description = "Users returned")
    @GetMapping
    public List<UserDto> listUsers() {
        return userService.listUsers();
    }

    @Operation(summary = "Get a user", description = "Returns a single user by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{userId}")
    public UserDto getUser(@Parameter(description = "User id") @PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @Operation(summary = "Register a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto registerUser(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.registerUser(createUserRequest);
    }

    @Operation(summary = "Update a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{userId}")
    public UserDto updateUser(@Parameter(description = "User id") @PathVariable Long userId, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
        return userService.updateUser(userId, updateUserRequest);
    }

    @Operation(summary = "Deactivate a user", description = "Soft-deletes a user by marking the account inactive.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deactivated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivateUser(@Parameter(description = "User id") @PathVariable Long userId) {
        userService.deactivateUser(userId);
    }
}
