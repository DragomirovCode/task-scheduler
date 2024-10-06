package ru.dragomirov.taskschedule.auth.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dragomirov.taskschedule.auth.*;
import ru.dragomirov.taskschedule.auth.login.LoginController;
import ru.dragomirov.taskschedule.auth.login.UserLoginDto;
import ru.dragomirov.taskschedule.auth.login.UserLoginMapper;
import ru.dragomirov.taskschedule.commons.jwt.JwtFilter;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserLoginMapper userLoginMapper; // Обновлено на UserLoginMapper

    @MockBean
    UserTokenGeneration userTokenGeneration;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JwtFilter jwtFilter;

    @SneakyThrows
    @Test
    void post_shouldReturnJwtToken() {
        // Подготовка данных
        User user = new User();
        user.setPassword("password000");
        user.setEmail("test000@gmail.com");
        String expectedToken = "generated-jwt-token";

        // Мокаем преобразование DTO в сущность User
        Mockito.when(userLoginMapper.toEntity(Mockito.any(UserLoginDto.class))).thenReturn(user);
        // Мокаем получение пользователя по email
        Mockito.when(userService.getByEmail("test000@gmail.com")).thenReturn(user);
        // Мокаем генерацию токена
        Mockito.when(userTokenGeneration.getToken(Mockito.any(User.class), Mockito.eq("password000")))
                .thenReturn(expectedToken);

        // Выполнение HTTP-запроса и проверка результата
        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\": \"password000\"," +
                                " \"email\": \"test000@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt-token").value(expectedToken));
    }
}
