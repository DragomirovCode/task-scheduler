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
    private final UserMapper userMapper;
    private final UserTokenGeneration userTokenGeneration;
    private static final System.Logger logger = System.getLogger(LoginController.class.getName());

    @PostMapping
    public ResponseEntity<Map<String, String>> post(
            @Valid @RequestBody UserLoginDto userLoginDto
    ) {
        User user = userMapper.toLoginEntity(userLoginDto);

        String token = userTokenGeneration.getToken(user);

        return ResponseEntity.ok(Map.of("jwt-token", token));
    }
}
