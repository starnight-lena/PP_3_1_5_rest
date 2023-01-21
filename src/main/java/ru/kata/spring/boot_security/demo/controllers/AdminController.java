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
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.validation.Valid;

@Controller
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }



    @PostMapping("/admin")
    public String  deleteUser(@RequestParam(required = true, defaultValue = "" ) Long userId,
                              @RequestParam(required = true, defaultValue = "" ) String action,
                              Model model) {
        if (action.equals("delete")){
            userService.deleteUser(userId);
        }
        return "redirect:/admin";
    }

    /*@GetMapping("/admin/gt/{userId}")
    public String  gtUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("allUsers", userService.usergtList(userId));
        return "admin";
    }*/

    @GetMapping("/new")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", userService.allRoles());

        return "new";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model) {
        try {
            if (userService.saveUser(userForm, bindingResult))
                return "redirect:/admin";
            else {
                model.addAttribute("allRoles", userService.allRoles());
                return "new";
            }
            //return userService.saveUser(userForm, bindingResult) ? "redirect:/admin" : "new";
        } catch (AssertionFailure | UnexpectedRollbackException e) {
            return "new";
        }
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(@ModelAttribute("id") Long id, Model model) {
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("allRoles", userService.allRoles());
        return "edit";
    }


    @PatchMapping("/admin/{id}/edit")
    public String update(@ModelAttribute("user") @Valid User updateuser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "edit";

        userService.update(updateuser);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String show(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("user", principal);
        return "user";
    }
}
