package comidev.authemail.components.user;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import comidev.authemail.components.role.RoleName;
import comidev.authemail.components.role.RoleService;
import comidev.authemail.components.user.dto.UserCreate;
import comidev.authemail.exceptions.HttpException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleService roleService;

    public Optional<UserEntity> findByUsernameOrEmail(String usernameOrEmail) {
        return userRepo.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    }

    public UserEntity getByUsernameOrEmail(String usernameOrEmail) {
        return findByUsernameOrEmail(usernameOrEmail).orElseThrow(() -> {
            String message = "Email o username NO existe";
            return new HttpException(HttpStatus.UNAUTHORIZED, message);
        });
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        return getByUsernameOrEmail(usernameOrEmail);
    }

    public UserEntity registerUser(UserCreate userCreate) {
        UserEntity userNEW = createUser(userCreate);
        return userRepo.save(userNEW);
    }

    public void registerUserOAuth2(UserCreate userCreate) {
        UserEntity userNEW = createUser(userCreate);
        userNEW.setEnabled(true);
        userRepo.save(userNEW);
    }

    private UserEntity createUser(UserCreate userCreate) {
        boolean existsUsernameOrEmail = userRepo.existsByUsernameOrEmail(
                userCreate.getUser().getUsername(),
                userCreate.getEmail());

        if (existsUsernameOrEmail) {
            String message = "El username o email YA existe!";
            throw new HttpException(HttpStatus.CONFLICT, message);
        }

        UserEntity userNEW = new UserEntity(userCreate);
        userNEW.getRoles().add(roleService.getByName(RoleName.CLIENTE));

        log.info("Usuario registrado :D -> {}", userNEW);
        return userNEW;
    }

    public void enableUser(String email) {
        UserEntity userDB = userRepo.findByEmail(email)
                .orElseThrow(() -> {
                    String message = "Email NO existe -> " + email;
                    return new HttpException(HttpStatus.NOT_FOUND, message);
                });

        userDB.setEnabled(true);
        userRepo.save(userDB);
    }
}
