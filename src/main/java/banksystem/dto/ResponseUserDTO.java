package banksystem.dto;

import banksystem.enums.Role;

public record ResponseUserDTO(
        Long id,
        String email,
        Role role
) {

}
