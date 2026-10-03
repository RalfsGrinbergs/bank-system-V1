package banksystem.service;

import banksystem.Dto.ResponseUserDTO;
import banksystem.Dto.UserDTO;
import banksystem.Entity.User;
import banksystem.Repository.UserRepository;
import banksystem.Service.UserService;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
}
