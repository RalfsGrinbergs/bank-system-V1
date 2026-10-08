package banksystem.service;

import banksystem.dto.ResponseUserDTO;
import banksystem.dto.UserDTO;
import banksystem.entity.User;
import banksystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static banksystem.enums.Role.CLIENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    @Test
    void addUserSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);
        UserDTO userToAdd = new UserDTO(
                 "email@example.com",
                "test-password"
        );
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ResponseUserDTO add = userService.addUser(userToAdd);
        assertEquals("email@example.com", add.email());



    }
    @Test
    void findById() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);
        User userToFind = new User(
                "email@example.com",
                "test-password",
                CLIENT
        );
        userToFind.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(userToFind));
        ResponseUserDTO userDTO = userService.findById(1L);
        assertEquals("email@example.com", userDTO.email());

    }
    @Test
    void findByIdNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        UserService userService = new UserService(userRepository, passwordEncoder);

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());
        assertThrows(
    EntityNotFoundException.class,
                () -> userService.findById(2L)
        );
    }
}
