package ru.dragomirov.taskschedule.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationDto;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationMapper;
import ru.dragomirov.taskschedule.auth.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class GetInfoUserController {
    private final UserService userService;
    private final UserRegistrationMapper userRegistrationMapper;

    @GetMapping
    public UserRegistrationDto get(
            Authentication authentication
    ) {
        String userName = authentication.getName();
        User user = userService.getByUsername(userName);

        return userRegistrationMapper.toDto(user);
    }
}
