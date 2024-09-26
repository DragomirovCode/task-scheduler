package ru.dragomirov.taskschedule.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[^\\s][\\S]*$", message = "Username cannot start with a space")
    public String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, message = "Password must be at least 1 characters long")
    @Pattern(regexp = "^\\S+$", message = "Password cannot contain spaces")
    public String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password confirmation cannot be blank")
    @Size(min = 1, message = "Password confirmation must be at least 1 characters long")
    @Pattern(regexp = "^\\S+$", message = "Password confirmation cannot contain spaces")
    public String passwordConfirmation;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Incorrect email format")
    public String email;

}
