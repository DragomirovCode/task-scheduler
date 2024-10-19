package ru.dragomirov.taskschedule.auth;

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
import ru.dragomirov.taskschedule.commons.DualServiceUserMapper;
import ru.dragomirov.taskschedule.commons.kafka.EmailProducer;
import ru.dragomirov.taskschedulercommondto.kafka.MessageDto;
import ru.dragomirov.taskschedulercommondto.kafka.UserDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailProducer emailProducer;
    private final DualServiceUserMapper dualServiceUserMapper;

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getById", key = "#id")
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#username")
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "UserService::getByUsername", key = "#email")
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username")}
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

            UserDto dto = dualServiceUserMapper.toDto(user);
            MessageDto messageDto = new MessageDto(dto, "REGISTRATION", new HashMap<>());
            emailProducer.sendEmailMessage(messageDto);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateException("A user with the same username or email already exists");
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
