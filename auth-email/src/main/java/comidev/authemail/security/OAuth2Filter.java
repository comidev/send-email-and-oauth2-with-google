package comidev.authemail.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import comidev.authemail.components.auth.AuthManager;
import comidev.authemail.components.user.dto.UserCreate;
import comidev.authemail.components.user.dto.UserLogin;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2Filter extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthManager authManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String name = user.getAttribute("name");
        String email = user.getAttribute("email");

        UserCreate userCreate = new UserCreate(
                name,
                email,
                new UserLogin(email, null));

        authManager.registerOrloginWithGoogle(userCreate);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
