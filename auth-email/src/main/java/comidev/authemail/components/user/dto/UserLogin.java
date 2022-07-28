package comidev.authemail.components.user.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    @NotEmpty(message = "No puede ser vacio uu")
    @Length(min = 3, message = "No puede ser menor a 3 caracteres")
    private String username;
    @NotEmpty(message = "No puede ser vacio uu")
    @Length(min = 3, message = "No puede ser menor a 3 caracteres")
    private String password;
}
