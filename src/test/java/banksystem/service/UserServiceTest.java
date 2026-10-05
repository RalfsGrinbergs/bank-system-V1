package banksystem.service;

import banksystem.Dto.ResponseUserDTO;
import banksystem.Dto.UserDTO;
import banksystem.Entity.User;
import banksystem.Repository.UserRepository;
import banksystem.Service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static banksystem.Enum.Role.CLIENT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    @Test
    void addUserSuccess() {
        UserRepository userRepository = mock(UserRepository.class);
        UserService userService = new UserService(userRepository);
        UserDTO userToAdd = new UserDTO(
                null, "email@example.com",
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
        UserService userService = new UserService(userRepository);
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
        UserService userService = new UserService(userRepository);

        when(userRepository.findById(2L))
                .thenReturn(Optional.empty());
        assertThrows(
    EntityNotFoundException.class,
                () -> userService.findById(2L)
        );
    }
}
