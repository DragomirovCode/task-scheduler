package ru.dragomirov.taskschedule.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.commons.ResourceNotFoundException;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserTokenGeneration {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder; // Добавляем PasswordEncoder
    private final UserService userService;

    public String getToken(User user, String rawPassword) {
        // Получаем пользователя по email
        User currentUser = userService.getByEmail(user.getEmail());

        // Сравниваем "сырой" пароль с хешированным
        if (!passwordEncoder.matches(rawPassword, currentUser.getPassword())) {
            throw new ResourceNotFoundException("User not found");
        }

        // Если пароли совпадают, аутентифицируем пользователя
        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(currentUser.getUsername(),
                        rawPassword);

        authenticationManager.authenticate(authInputToken);

        // Генерируем JWT-токен
        return jwtTokenProvider.generateToken(currentUser.getUsername());
    }

    public String createToken(User user) {
        return jwtTokenProvider.generateToken(user.getUsername());
    }

}
