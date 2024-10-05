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
    private final UserRegistrationMapper userRegistrationMapper;
    private final UserTokenGeneration userTokenGeneration;

    @PostMapping
    public Map<String, String> post(
            @Valid @RequestBody UserRegistrationDto userRegistrationDto
    ) {

        User user = userRegistrationMapper.toEntity(userRegistrationDto);
        userService.save(user);

        String token = userTokenGeneration.createToken(user);

        return Map.of("jwt-token", token);
    }
}
