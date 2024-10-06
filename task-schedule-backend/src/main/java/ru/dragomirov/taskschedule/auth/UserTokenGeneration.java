package ru.dragomirov.taskschedule.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserTokenGeneration {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public String getToken(User user, String rawPassword) {

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), rawPassword);

        authenticationManager.authenticate(authInputToken);

        return jwtTokenProvider.generateToken(user.getUsername());
    }

    public String createToken(User user) {
        return jwtTokenProvider.generateToken(user.getUsername());
    }

}
