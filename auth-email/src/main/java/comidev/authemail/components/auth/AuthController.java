package comidev.authemail.components.auth;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import comidev.authemail.components.user.dto.UserCreate;
import comidev.authemail.components.user.dto.UserLogin;
import comidev.authemail.exceptions.Validator;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auths")
@AllArgsConstructor
public class AuthController {
    private final AuthManager authManager;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody()
    public void registerUser(@Valid @RequestBody UserCreate userCreate,
            BindingResult bindingResult) {
        Validator.checkOrThrowBadRequest(bindingResult);

        authManager.register(userCreate);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public void login(@Valid @RequestBody UserLogin userCreate,
            BindingResult bindingResult) {
        Validator.checkOrThrowBadRequest(bindingResult);

        authManager.login(userCreate);
    }

    @GetMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody()
    public void confirm(@RequestParam("token") String token) {
        authManager.confirm(token);
    }
}
