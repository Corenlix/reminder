package com.test.reminder.controller;

import com.test.reminder.dto.UserDto;
import com.test.reminder.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/settings")
    public String getUserSettings(Model model) {
        UserDto userDto = userService.getCurrentUser();

        model.addAttribute("user", userDto);
        return "user/settings";
    }

    @PostMapping("/settings")
    public String updateUserSettings(@ModelAttribute("user") @Validated UserDto user) {
        userService.updateUser(user);
        return "redirect:/user/settings?success";
    }
}
