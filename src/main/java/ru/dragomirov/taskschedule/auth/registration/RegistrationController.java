package ru.dragomirov.taskschedule.auth.registration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.dragomirov.taskschedule.auth.*;

import java.util.Map;


@RestController
@RequestMapping("/api/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final UserTokenGeneration userTokenGeneration;

    @PostMapping
    public Map<String, String> post(
            @Valid @RequestBody UserDto userDto
    ) {

        User user = userMapper.toEntity(userDto);
        userService.save(user);

        Object token = userTokenGeneration.getToken(user);

        return Map.of("jwt-token", token.toString());
    }
}
