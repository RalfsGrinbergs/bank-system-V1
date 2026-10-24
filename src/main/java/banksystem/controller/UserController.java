package banksystem.controller;

import banksystem.configuration.JwtFilter;
import banksystem.configuration.JwtService;
import banksystem.dto.LoginRequestDTO;
import banksystem.dto.ResponseUserDTO;
import banksystem.dto.TokenResponseDTO;
import banksystem.dto.UserDTO;
import banksystem.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/registration")
    public ResponseEntity<ResponseUserDTO> addUser(@Valid @RequestBody UserDTO userToAdd) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.addUser(userToAdd));
    }
    @GetMapping("/admin")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ResponseUserDTO>> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ResponseUserDTO> findByEmail(Authentication authentication){
        String email = authentication.getName();
        return ResponseEntity.ok(userService.findByEmail(email));
    }
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new TokenResponseDTO(jwtService.generateToken(authentication.getName(), roles)));

    }

}
