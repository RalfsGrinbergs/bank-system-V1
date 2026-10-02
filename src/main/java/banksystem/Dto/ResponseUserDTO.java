package banksystem.Dto;

import banksystem.Enum.Role;

public record ResponseUserDTO(
        Long id,
        String email,
        Role role
) {

}
