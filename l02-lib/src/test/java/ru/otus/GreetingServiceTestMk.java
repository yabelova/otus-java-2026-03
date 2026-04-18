package ru.otus;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
public class GreetingServiceTestMk {
    @Mock
    private UserRepository userRepository;

    @Mock
    private GreetingService greetingService;

    @InjectMocks
    private UserService userService;

    @Test
    void should_build_greeting_for_existing_user() {
        //given
        User user = new User(11L, "Eleven");
        when(userRepository.findById(11L)).thenReturn(Optional.of(user));
        when(greetingService.greet("Eleven")).thenReturn("Hello, Eleven");

        //when
        String result = userService.buildGreetingForUser(11L);

        //then
        assertEquals("Hello, Eleven", result);
        verify(userRepository).findById(11L);
        verify(greetingService).greet("Eleven");
    }

    @Test
    void should_fail_greeting_for_non_existing_user() {
        //given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        //when&then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.buildGreetingForUser(99L);
        });
        assertEquals("User not found: 99", exception.getMessage());
        verifyNoInteractions(greetingService);
    }

    @Test
    void should_call_repository_exactly_once() {
        //given
        User user = new User(11L, "Eleven");
        when(userRepository.findById(11L)).thenReturn(Optional.of(user));
        when(greetingService.greet(anyString())).thenReturn("Hi");

        //when
        userService.buildGreetingForUser(11L);

        //then
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).findById(12L);
    }
}