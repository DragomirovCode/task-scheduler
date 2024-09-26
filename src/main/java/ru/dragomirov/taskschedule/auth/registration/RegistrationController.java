package ru.dragomirov.taskschedule.auth.registration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserDto;
import ru.dragomirov.taskschedule.auth.UserMapper;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

import java.util.Map;


@RestController
@RequestMapping("/api/auth/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public Map<String, String> post(
            @Valid @RequestBody UserDto userDto
    ) {

        User user = userMapper.toEntity(userDto);
        userService.save(user);
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return Map.of("jwt-token", token);
    }
}
