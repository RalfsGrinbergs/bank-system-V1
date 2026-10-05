package banksystem.Service;

import banksystem.Dto.ResponseUserDTO;

import banksystem.Dto.UserDTO;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }
    @Transactional
   public ResponseUserDTO addUser(UserDTO userToAdd) {
    var userToSave = new User(
            userToAdd.email(),
            userToAdd.password(),
            Role.USER
    );
    var savedUser = userRepository.save(userToSave);

    return toDomainUser(savedUser);


   }
   public List<ResponseUserDTO> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream()
            .map(this::toDomainUser).toList();
   }
   private ResponseUserDTO toDomainUser(User user) {
    return new ResponseUserDTO(
            user.getId(),
            user.getEmail(),
            user.getRole()
    );
   }
}
