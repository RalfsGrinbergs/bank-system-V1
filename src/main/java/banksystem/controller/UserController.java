package banksystem.controller;

import banksystem.dto.ResponseUserDTO;
import banksystem.dto.UserDTO;
import banksystem.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ResponseUserDTO> addUser(@Valid @RequestBody UserDTO userToAdd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(userToAdd));
    }
    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseUserDTO> findByEmail(Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(userService.findByEmail(email));
    }

}
