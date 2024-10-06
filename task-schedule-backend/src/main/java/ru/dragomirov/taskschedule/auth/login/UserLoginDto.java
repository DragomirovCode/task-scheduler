package ru.dragomirov.taskschedule.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Incorrect email format")
    public String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, message = "Password must be at least 1 characters long")
    public String password;

}
