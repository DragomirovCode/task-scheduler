package ru.dragomirov.taskschedule.auth;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dragomirov.taskschedule.commons.DuplicateException;
import ru.dragomirov.taskschedule.commons.ResourceNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getByById(Long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username") }
    )
    public void save(User user) {
        try {
            if (!user.getPassword().equals(user.getPasswordConfirmation())) {
                throw new IllegalStateException("Password and password confirmation do not match");
            }
            user.setRoles(Collections.singleton(Role.ROLE_USER));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setPasswordConfirmation(passwordEncoder.encode(user.getPasswordConfirmation()));
            userRepository.save(user);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                String message = e.getMostSpecificCause().getMessage();
                if (message.contains("username")) {
                    throw new DuplicateException("A user with this username already exists");
                } else if (message.contains("email")) {
                    throw new DuplicateException("A user with this email already exists");
                }
            }
        }
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#updatePerson.id"),
            @CachePut(value = "UserService::getByUsername", key = "#updatePerson.username")}
    )
    public void update(Long id, User updatePerson) {
        updatePerson.setId(id);
        userRepository.save(updatePerson);
    }

    @Transactional
    @CacheEvict(value = "UserService::delete", key = "#id")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
