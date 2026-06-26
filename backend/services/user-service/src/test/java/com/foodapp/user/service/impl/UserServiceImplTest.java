package com.foodapp.user.service.impl;

import com.foodapp.user.dto.CreateUserRequest;
import com.foodapp.user.dto.UpdateUserRequest;
import com.foodapp.user.dto.UserDto;
import com.foodapp.user.entity.Role;
import com.foodapp.user.entity.User;
import com.foodapp.user.exception.ResourceNotFoundException;
import com.foodapp.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final Long USER_ID = 1L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void listUsers_mapsEveryRowToDto() {
        when(userRepository.findAll()).thenReturn(List.of(
                user("Hemal", "hzora@example.com"),
                user("Karan", "karan@example.com")));

        List<UserDto> result = userService.listUsers();

        assertEquals(2, result.size());
        assertEquals("hzora@example.com", result.get(0).email());
    }

    @Test
    void getUser_returnsDto_whenFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user("Hemal", "hzora@example.com")));

        UserDto dto = userService.getUser(USER_ID);

        assertEquals("Hemal", dto.name());
        assertEquals("CUSTOMER", dto.role());
        assertTrue(dto.active());
    }

    @Test
    void getUser_throwsNotFound_whenMissing() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(USER_ID));
        assertTrue(ex.getMessage().contains("not found"));
    }

    @Test
    void register_hashesPasswordBeforeSaving() {
        when(passwordEncoder.encode("rawSecret")).thenReturn("ENCODED_HASH");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        CreateUserRequest request = new CreateUserRequest("New User", "new@example.com", "rawSecret",
                Role.CUSTOMER, "999", "MALE", "Patiala");

        UserDto dto = userService.registerUser(request);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("ENCODED_HASH", saved.getPasswordHash());
        assertNotEquals("rawSecret", saved.getPasswordHash());
        verify(passwordEncoder).encode("rawSecret");
        assertEquals("new@example.com", dto.email());
    }

    @Test
    void update_appliesChanges_withoutTouchingPassword() {
        User existing = user("Old Name", "old@example.com");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existing));
        UpdateUserRequest request = new UpdateUserRequest("New Name", "222", "FEMALE", "New Addr");

        UserDto dto = userService.updateUser(USER_ID, request);

        assertEquals("New Name", dto.name());
        assertEquals("222", dto.phone());
        assertEquals("New Addr", dto.address());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void update_throwsNotFound_whenMissing() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(USER_ID, new UpdateUserRequest("X", null, null, null)));
    }

    @Test
    void deactivate_setsActiveFalse() {
        User existing = user("Hemal", "hzora@example.com");
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(existing));

        userService.deactivateUser(USER_ID);

        assertFalse(existing.isActive());
    }

    @Test
    void deactivate_throwsNotFound_whenMissing() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deactivateUser(USER_ID));
    }

    private User user(String name, String email) {
        return new User(name, email, "ENCODED", Role.CUSTOMER, "111", "MALE", "Patiala");
    }
}
