package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping()
public class UserController {

    private UserServiceImpl userService;
    private RoleServiceImpl roleService;

    @Autowired
    public UserController(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registartionPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleService.getRoles());
        userService.getAllUsers();

        roleService.getRoles();
        return "/registration";
    }

    @PostMapping("/registration")
    public String perfomRegistration(@ModelAttribute("user") User user, BindingResult bindingResult) {

        userService.saveUser(user);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/user")
    public String showUserInfo(Model model, Principal principal) {
        model.addAttribute("user", userService.getUserByUsername(principal.getName()));
        return "user";
    }

}
