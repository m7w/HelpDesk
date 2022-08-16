package com.training.helpdesk.user.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;
import com.training.helpdesk.user.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final Long USER_ID = 1L;
    private static final User USER = new User(USER_ID, "Lenny", "Leonart", Role.ROLE_EMPLOYEE, 
            "user1_mogilev@yopmail.com", "password");

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testFindById() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(USER));

        User actual = userService.findById(USER_ID);

        assertEquals(USER, actual);
        verify(userRepository).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testFindById_NotFound() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        String expected = "User with id=1 not found";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
                () -> userService.findById(USER_ID));
        
        assertEquals(expected, exception.getMessage());
        verify(userRepository).findById(USER_ID);
        verifyNoMoreInteractions(userRepository);
    }
}
