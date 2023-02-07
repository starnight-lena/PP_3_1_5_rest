package ru.kata.spring.boot_security.demo.controllers;

import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;

    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        model.addAttribute("userAuthorized", principal);
        return "admin";
    }


    @GetMapping("/new")
    public String getCreationUserForm(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        model.addAttribute("userAuthorized", principal);

        return "/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, @CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        try {
            if (userService.saveUser(userForm, bindingResult))
                return "redirect:/admin";
            else {
                model.addAttribute("allRoles", roleService.allRoles());
                model.addAttribute("userAuthorized", principal);
                return "/new";
            }
        } catch (AssertionFailure | UnexpectedRollbackException e) {
            model.addAttribute("userAuthorized", principal);
            return "/new";
        }
    }


    @GetMapping("/edit/{id}")
    public String updateUser(@ModelAttribute("user") User updateuser, BindingResult bindingResult, @PathVariable("id") Long id) {
        if (bindingResult.hasErrors()) {
            return "redirect:/edit/{id}";
        }
        userService.updateUser(id, updateuser);
        return "redirect:/admin";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
