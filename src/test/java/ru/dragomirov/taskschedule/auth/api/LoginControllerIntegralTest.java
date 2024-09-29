package ru.dragomirov.taskschedule.auth.api;

import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerIntegralTest {

    @Autowired
    MockMvc mockMvc;

    @Container
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("database")
            .withUsername("my_mysql_username")
            .withPassword("my_mysql_password");

    @Container
    static final GenericContainer<?> redisContainer = new GenericContainer<>("redis:7.4.0")
            .withExposedPorts(6379);

    static {
        mySQLContainer.start();
        redisContainer.start();
    }

    @DynamicPropertySource
    static void mysqlProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        registry.add("spring.redis.host", redisContainer::getHost);
        registry.add("spring.redis.port", () -> redisContainer.getMappedPort(6379));
    }

    @SneakyThrows
    @Order(1)
    @Test
    void post_shouldReturnJwtToken_Register() {
        String requestBody = "{\"username\": \"username000\",\"password\": \"password000\"," +
                " \"passwordConfirmation\": \"password000\" ,\"email\": \"test000@gmail.com\"}";

        mockMvc.perform(post("/api/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt-token").exists());
    }

    @SneakyThrows
    @Order(2)
    @Test
    void post_shouldReturnJwtToken_Login() {
        String requestBody = "{\"username\": \"username000\",\"password\": \"password000\"," +
                " \"passwordConfirmation\": \"password000\" ,\"email\": \"test000@gmail.com\"}";

        mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt-token").exists());
    }
}
