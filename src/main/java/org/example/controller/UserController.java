package org.example.controller;

import jakarta.validation.Valid;
import org.example.model.User;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    public final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/email/{email}")
    public User getEmail(@PathVariable String email){
        return userService.getEmail(email);
    }

    @GetMapping("/user/{userId}")
    public User getUser(@PathVariable long userId){
        return userService.getUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@Valid @RequestBody User user){
        return userService.createUser(user.getName(), user.getEmail());
    }
}
