package com.example.pems.service;

import com.example.pems.entity.User;
import com.example.pems.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import com.example.pems.dto.RegistrationRequest;
import com.example.pems.dto.UpdateProfileRequest;
import com.example.pems.entity.User.Role;
import com.example.pems.exception.BadRequestException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(User user) {
        user.setRole(Role.USER);
        user.setActive(true);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        return userRepository.save(user);
    }

    public User registerUser(RegistrationRequest request) {
        if (emailExists(request.email())) {
            throw new BadRequestException("Email is already registered");
        }
        return createUser(new User(request.name(), request.email(), request.password()));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> updateUser(Long id, User updatedUser) {

        return userRepository.findById(id)
                .map(existingUser -> {

                    existingUser.setName(updatedUser.getName());
                    existingUser.setEmail(updatedUser.getEmail());

                    // Password ko dobara hash karo
                    existingUser.setPassword(
                            passwordEncoder.encode(
                                    updatedUser.getPassword()
                            )
                    );

                    return userRepository.save(existingUser);
                });
    }

    public Optional<User> updateOwnProfile(String email, UpdateProfileRequest request) {
        return userRepository.findByEmail(email)
                .map(user -> {
                    user.setName(request.name());
                    user.setPassword(passwordEncoder.encode(request.password()));
                    return userRepository.save(user);
                });
    }

    public Optional<User> setActive(Long id, boolean active) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setActive(active);
                    return userRepository.save(user);
                });
    }

    public boolean verifyPassword(String rawPassword,
                                  String encodedPassword) {

        return passwordEncoder.matches(
                rawPassword,
                encodedPassword
        );
    }
}
