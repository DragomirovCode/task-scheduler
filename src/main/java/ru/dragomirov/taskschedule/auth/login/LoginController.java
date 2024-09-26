package ru.dragomirov.taskschedule.auth.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserDto;
import ru.dragomirov.taskschedule.auth.UserMapper;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<Map<String, String>> post(
            @Valid @RequestBody UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(),
                        user.getPassword());

        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Incorrect credentials!"));
        }

        String token = jwtTokenProvider.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("jwt-token", token));
    }
}
