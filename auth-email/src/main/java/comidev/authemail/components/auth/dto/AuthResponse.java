package comidev.authemail.components.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthResponse {
    private String username;

    public AuthResponse(String username) {
        this.username = username;
    }
}
