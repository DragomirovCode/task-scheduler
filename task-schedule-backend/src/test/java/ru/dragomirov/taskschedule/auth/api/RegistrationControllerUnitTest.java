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
import ru.dragomirov.taskschedule.auth.registration.RegistrationController;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationDto;
import ru.dragomirov.taskschedule.auth.registration.UserRegistrationMapper;
import ru.dragomirov.taskschedule.commons.jwt.JwtFilter;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class RegistrationControllerUnitTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    UserRegistrationMapper userRegistrationMapper;

    @MockBean
    UserTokenGeneration userTokenGeneration;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    JwtFilter jwtFilter;

    @SneakyThrows
    @Test
    void post_shouldReturnJwtToken() {
        User user = new User();
        user.setUsername("username000");
        user.setPassword("password000");
        user.setPasswordConfirmation("password000");
        user.setEmail("test000@gmail.com");
        String expectedToken = "generated-jwt-token";

        Mockito.when(userRegistrationMapper.toEntity(Mockito.any(UserRegistrationDto.class))).thenReturn(user);
        Mockito.doNothing().when(userService).save(Mockito.any(User.class));
        Mockito.when(userTokenGeneration.createToken(Mockito.any(User.class))).thenReturn(expectedToken);

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"username000\",\"password\": \"password000\"," +
                                " \"passwordConfirmation\": \"password000\" ,\"email\": \"test000@gmail.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt-token").value(expectedToken));
    }
}
