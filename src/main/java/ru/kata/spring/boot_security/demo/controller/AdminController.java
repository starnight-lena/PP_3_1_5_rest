package ru.kata.spring.boot_security.demo.controller;

import org.hibernate.AssertionFailure;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userService;

    private final RoleServiceImpl roleService;

    public AdminController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String getAllUsers(@AuthenticationPrincipal User principal, Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        model.addAttribute("userAuthorized", principal);
        return "admin";
    }


    @GetMapping("/new")
    public String getCreationUserForm(@AuthenticationPrincipal User principal, Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", roleService.allRoles());
        model.addAttribute("userAuthorized", principal);

        return "/new";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, @AuthenticationPrincipal User principal, Model model) {
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


    @GetMapping("/edit/")
    public String updateUser(@ModelAttribute("user") User updateuser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/edit/";
        }
        userService.updateUser(updateuser);
        return "redirect:/admin";
    }

    @GetMapping("/delete/")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin";
    }

}
