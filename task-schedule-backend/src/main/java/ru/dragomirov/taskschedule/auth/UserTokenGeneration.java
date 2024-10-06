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
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public String getToken(User user, String rawPassword) {
        User currentUser = userService.getByEmail(user.getEmail());

        if (!passwordEncoder.matches(rawPassword, currentUser.getPassword())) {
            throw new ResourceNotFoundException("User not found");
        }

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(currentUser.getUsername(),
                        rawPassword);

        authenticationManager.authenticate(authInputToken);

        return jwtTokenProvider.generateToken(currentUser.getUsername());
    }

    public String createToken(User user) {
        return jwtTokenProvider.generateToken(user.getUsername());
    }

}
