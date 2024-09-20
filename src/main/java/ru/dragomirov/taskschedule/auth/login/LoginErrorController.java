package ru.dragomirov.taskschedule.auth.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginErrorController {
    @GetMapping("/error401")
    public String get() {
        return "auth-error/error401";
    }
}