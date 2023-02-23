package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> allUsers();

    boolean saveUser(User user, BindingResult bindingResult);

    void deleteUser(Long id);

    User updateUser(User user);

    User findUserById(Long id);

}
