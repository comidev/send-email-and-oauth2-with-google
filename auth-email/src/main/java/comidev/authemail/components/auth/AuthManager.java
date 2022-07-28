package comidev.authemail.components.auth;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import comidev.authemail.components.token.ConfirmationTokenService;
import comidev.authemail.components.user.UserEntity;
import comidev.authemail.components.user.UserService;
import comidev.authemail.components.user.dto.UserCreate;
import comidev.authemail.components.user.dto.UserLogin;
import comidev.authemail.exceptions.HttpException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthManager {
    private final UserService userService;
    private final BCryptPasswordEncoder bcrypt;
    private final AuthenticationManager authManager;
    private final ConfirmationTokenService tokenService;

    @Transactional
    public void register(UserCreate userCreate) {
        String password = userCreate.getUser().getPassword();
        userCreate.getUser().setPassword(bcrypt.encode(password));
        UserEntity userDB = userService.registerUser(userCreate);

        tokenService.save(userDB);

        log.info("Registrado! :D");
    }

    public void login(UserLogin userLogin) {
        Authentication auth;
        try {
            auth = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLogin.getUsername(),
                            userLogin.getPassword()));
        } catch (DisabledException e) {
            String message = "Revise su gmail :v le mandamos un token";
            UserEntity userDB = userService.getByUsernameOrEmail(userLogin.getUsername());

            tokenService.checkAndResendToken(userDB);

            throw new HttpException(HttpStatus.UNAUTHORIZED, message);

        } catch (BadCredentialsException e) {
            String message = "Username y/o password incorrecto(s)";
            throw new HttpException(HttpStatus.UNAUTHORIZED, message);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
        log.info("Logeado :D!");
    }

    public void registerOrloginWithGoogle(UserCreate userCreate) {
        Optional<UserEntity> userOpt = userService.findByUsernameOrEmail(
                userCreate.getEmail());

        if (userOpt.isEmpty()) {
            userService.registerUserOAuth2(userCreate);
        } else {
            UserEntity userDB = userOpt.get();

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    userDB.getUsername(),
                    null,
                    userDB.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("Logeado! :D");
        }
    }

    @Transactional
    public void confirm(String token) {
        String email = tokenService.confirmToken(token);
        userService.enableUser(email);
    }
}
