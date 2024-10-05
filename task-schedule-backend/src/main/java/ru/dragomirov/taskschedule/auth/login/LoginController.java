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
    private final UserLoginMapper userLoginMapper;
    private final UserTokenGeneration userTokenGeneration;

    @PostMapping
    public ResponseEntity<Map<String, String>> post(
            @Valid @RequestBody UserLoginDto userLoginDto
    ) {
       User user = userLoginMapper.toEntity(userLoginDto);

       String token = userTokenGeneration.getToken(user);

       return ResponseEntity.ok(Map.of("jwt-token", token));
    }
}
