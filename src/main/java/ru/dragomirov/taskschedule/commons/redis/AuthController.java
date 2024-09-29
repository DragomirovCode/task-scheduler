package ru.dragomirov.taskschedule.commons.redis;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/logout")
public class AuthController {

    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(TokenBlacklistService tokenBlacklistService, JwtTokenProvider jwtTokenProvider) {
        this.tokenBlacklistService = tokenBlacklistService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        long expiration = jwtTokenProvider.getExpiration(token);
        tokenBlacklistService.blacklistToken(token, expiration);

        return ResponseEntity.ok("Logout successful");
    }
}
