package ru.dragomirov.taskschedule.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.commons.DualServiceUserMapper;
import ru.dragomirov.taskschedule.auth.UserService;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.List;

@RestController
@RequestMapping("/api/user/get-all")
@RequiredArgsConstructor
public class GetAllUserController {
    private final UserService userService;
    private final DualServiceUserMapper dualServiceUserMapper;

    @GetMapping
    public List<UserDto> get() {
        List<User> users = userService.getAll();
        return dualServiceUserMapper.toDto(users);
    }
}
