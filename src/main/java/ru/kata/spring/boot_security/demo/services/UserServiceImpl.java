package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ru.kata.spring.boot_security.demo.entities.Role;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.repositories.*;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @PersistenceContext
    private EntityManager em;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean searchUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean searchUserByEmail(String email) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.email LIKE :paramEmail", User.class)
                .setParameter("paramEmail", email).getResultStream().findFirst().orElse(null);;

        if (user == null) {
            return false;
        } else {
            return true;
        }
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    public List<Role> allRoles() {
        return roleRepository.findAll();
    }



    public boolean saveUser(User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return false;
        }
        if (searchUserByUsername(user.getUsername())) {
            return false;
        }

        if (searchUserByEmail(user.getEmail())) {
            return false;
        }


        user.setRoles(user.getRoles());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    //@Transactional
    public void update(User updateUser) {
        if (updateUser.getPassword() != "") {
            updateUser.setPassword(bCryptPasswordEncoder.encode(updateUser.getPassword()));
        }
        em.merge(updateUser);
    }

}
