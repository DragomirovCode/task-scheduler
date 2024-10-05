package ru.dragomirov.taskschedule.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    public String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 1, message = "Password must be at least 1 characters long")
    public String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password confirmation cannot be blank")
    @Size(min = 1, message = "Password confirmation must be at least 1 characters long")
    public String passwordConfirmation;

   @NotBlank(message = "Email cannot be blank")
   @Email(message = "Incorrect email format")
   public String email;

}
