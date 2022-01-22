package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("roles", roleService.getAllRole());
        return "/admin";
    }

    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String showNewUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRole());
        return "new_user";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String saveNewUser(@ModelAttribute("user") User user, @RequestParam(value = "roleId", required = false) Long[] roleId) {
        if (roleId != null) {
            user.getRoles().addAll(Arrays.stream(roleId).map(id -> roleService.getRoleById(id)).collect(Collectors.toSet()));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/{id}/edit", method = RequestMethod.GET)
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roleService.getAllRole());
        return "/edit_user";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    public String editUser(@ModelAttribute("user") User user, @RequestParam(value = "roleId", required = false) Long[] roleId) {
        if (roleId != null) {
            user.getRoles().addAll(Arrays.stream(roleId).map(id -> roleService.getRoleById(id)).collect(Collectors.toSet()));
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    private String delete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
