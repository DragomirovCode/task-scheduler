package ru.dragomirov.taskschedulefront;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error-not-found-page")
    public String handleError() {
        // Возвращаем кастомный шаблон для ошибки
        return "error/customError-404";
    }
}
