package ru.dragomirov.taskschedule.auth.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserTokenGeneration userTokenGeneration;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Map<String, String>> post(
            @Valid @RequestBody UserLoginDto userLoginDto
    ) {
        User user = userService.getByEmail(userLoginDto.email);

        String token = userTokenGeneration.getToken(user, userLoginDto.password);

        return ResponseEntity.ok(Map.of("jwt-token", token));
    }
}
