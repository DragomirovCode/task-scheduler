package ru.dragomirov.taskschedule.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.auth.login.LoginController;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

@Service
@RequiredArgsConstructor
public class UserTokenGeneration {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private static final System.Logger logger = System.getLogger(UserTokenGeneration.class.getName());

    public String getToken(User user) {
        User currentUser = userService.getByEmail(user.getEmail());

        logger.log(System.Logger.Level.ERROR, currentUser);

        UsernamePasswordAuthenticationToken authInputToken =
                new UsernamePasswordAuthenticationToken(currentUser.getUsername(),
                        currentUser.getPassword());

        authenticationManager.authenticate(authInputToken);

        return jwtTokenProvider.generateToken(currentUser.getUsername());
    }

    public String createToken(User user) {
        return jwtTokenProvider.generateToken(user.getUsername());
    }

}
