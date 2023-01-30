package ru.kata.spring.boot_security.demo.controllers;

import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.exceptions.TemplateProcessingException;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.validation.Valid;

@Controller
public class AdminController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/admin")
    public String userList(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", userService.allRoles());
        model.addAttribute("user", principal);
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


    @GetMapping("/new")
    public String registration(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("allRoles", userService.allRoles());
        model.addAttribute("user", principal);

        return "new";
    }

    @PostMapping("/new")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult,@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        try {
            if (userService.saveUser(userForm, bindingResult))
                return "redirect:/admin";
            else {
                model.addAttribute("allRoles", userService.allRoles());
                model.addAttribute("user", principal);
                return "new";
            }
            //return userService.saveUser(userForm, bindingResult) ? "redirect:/admin" : "new";
        } catch (AssertionFailure | UnexpectedRollbackException e) {
            model.addAttribute("user", principal);
            return "new";
        }
    }



    @PostMapping("/admin/{id}/edit")
    public String update(@ModelAttribute("user") @Valid User updateuser, BindingResult bindingResult, RedirectAttributes atts) {
        if (bindingResult.hasErrors()) {
            atts.addAttribute("hasErrors", true);
            return "admin";
        }


        userService.update(updateuser);
        return "redirect:/admin";
    }



    @GetMapping("/user")
    public String show(@CurrentSecurityContext(expression = "authentication.principal") User principal, Model model) {
        model.addAttribute("user", principal);
        return "user";
    }

    @GetMapping("/deleteUser")
    @ResponseBody
    public User deleteUser(Long userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("/editUser")
    @ResponseBody
    public User editUser(Long userId) {

        return userService.findUserById(userId);
    }

}
