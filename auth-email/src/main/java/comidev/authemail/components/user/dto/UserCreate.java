package comidev.authemail.components.user.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreate {
    @NotEmpty(message = "No puede ser vacio")
    private String name;
    @NotEmpty(message = "No puede ser vacio")
    @Email(message = "Mal formato email")
    private String email;
    @Valid
    private UserLogin user;
}
