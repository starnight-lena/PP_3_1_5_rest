package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> allUsers();

    boolean saveUser(User user, BindingResult bindingResult);

    void deleteUser(Long id);

    User updateUser(Long id, User user);

    User findUserById(Long id);

    public User loadUserByUsername(String username);
}
