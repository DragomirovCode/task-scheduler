package ru.dragomirov.taskschedule.commons.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.dragomirov.taskschedule.auth.User;
import ru.dragomirov.taskschedule.auth.UserRepository;
import ru.dragomirov.taskschedule.commons.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new JwtEntity(user);
    }
}
