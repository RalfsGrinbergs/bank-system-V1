package banksystem.Service;

import banksystem.Dto.ResponseUserDTO;

import banksystem.Dto.UserDTO;
import banksystem.Entity.User;
import banksystem.Enum.Role;
import banksystem.Repository.UserRepository;
import banksystem.exceptions.UserCreatingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }
    @Transactional
   public ResponseUserDTO addUser(UserDTO userToAdd) {
        if (userRepository.findByEmail(userToAdd.email()).isPresent()) {
            throw new UserCreatingException("User with that email already exists");
        }
    var userToSave = new User(
            userToAdd.email(),
            passwordEncoder.encode(userToAdd.password()),
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
    public ResponseUserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with that id" + id));
        return toDomainUser(user);
    }
   private ResponseUserDTO toDomainUser(User user) {
    return new ResponseUserDTO(
            user.getId(),
            user.getEmail(),
            user.getRole()
    );
   }
}
