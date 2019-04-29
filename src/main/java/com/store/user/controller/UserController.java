package com.store.user.controller;

import com.store.user.exception.UserNotFoundException;
import com.store.user.jpa.entity.User;
import com.store.user.model.request.UserRequest;
import com.store.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public ResponseEntity insertUser(@RequestBody UserRequest userRequest) {
        userService.insertUser(userRequest);
        log.info("Creating new user");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/list")
    public ResponseEntity getUserList() {
        log.info("Getting user list");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {

        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            log.error("User ID not found ");
            throw new UserNotFoundException("User Not Found");
        }
        log.info(String.format("Fetched User with id: %d ", id));
        return ResponseEntity.ok(user);

    }

}
