package banksystem.Controller;

import banksystem.Dto.ResponseUserDTO;
import banksystem.Dto.UserDTO;
import banksystem.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ResponseUserDTO> addUser( @RequestBody UserDTO userToAdd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(userToAdd));
    }
    @GetMapping
    public ResponseEntity<List<ResponseUserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ResponseUserDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(userService.findById(id));
    }

}
