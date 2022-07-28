package comidev.authemail.components.role;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleService {
    private final RoleRepo roleRepo;

    public Role getByName(RoleName roleName) {
        return roleRepo.findByName(roleName)
                .orElse(roleRepo.save(new Role(roleName)));
    }
}
