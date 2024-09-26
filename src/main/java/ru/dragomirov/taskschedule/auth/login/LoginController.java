package ru.dragomirov.taskschedule.auth.login;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserDto;
import ru.dragomirov.taskschedule.auth.UserMapper;
import ru.dragomirov.taskschedule.auth.UserTokenGeneration;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {
    private final UserMapper userMapper;
    private final UserTokenGeneration userTokenGeneration;

    @PostMapping
    public ResponseEntity<Map<String, String>> post(
            @Valid @RequestBody UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);

        Object token = userTokenGeneration.getToken(user);

        return ResponseEntity.ok(Map.of("jwt-token", token.toString()));
    }
}
