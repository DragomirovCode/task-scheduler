package ru.dragomirov.taskschedule.commons.redis;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.dragomirov.taskschedule.commons.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/logout")
@RequiredArgsConstructor
public class AuthController {
    private final TokenBlacklistService tokenBlacklistService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        long expiration = jwtTokenProvider.getExpiration(token);
        tokenBlacklistService.blacklistToken(token, expiration);

        return ResponseEntity.ok("Logout successful");
    }
}
