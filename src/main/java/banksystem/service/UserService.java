package banksystem.service;

import banksystem.dto.ResponseUserDTO;

import banksystem.dto.UserDTO;
import banksystem.entity.User;
import banksystem.enums.Role;
import banksystem.repository.UserRepository;
import banksystem.exceptions.UserCreatingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {
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
    public ResponseUserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with that email" + email));
        return toDomainUser(user);
    }
   private ResponseUserDTO toDomainUser(User user) {
    return new ResponseUserDTO(
            user.getId(),
            user.getEmail(),
            user.getRole()
    );
   }
   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with that email not found"));
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
   }

}
