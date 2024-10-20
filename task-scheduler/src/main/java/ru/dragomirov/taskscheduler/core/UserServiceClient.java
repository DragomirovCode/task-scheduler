package ru.dragomirov.taskscheduler.core;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.dragomirov.taskscheduler.commons.message.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceClient {
    private final RestTemplate restTemplate;

    public List<UserDto> getAllUsers() {
        ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                "http://localhost:8080/api/user/get-all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserDto>>() {}
        );
        return response.getBody();
    }
}
