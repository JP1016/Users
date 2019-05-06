package com.store.user.controller;

import com.store.user.exception.UserNotFoundException;
import com.store.user.jpa.entity.User;
import com.store.user.model.request.OrderRequest;
import com.store.user.model.request.UserRequest;
import com.store.user.model.response.UserResponse;
import com.store.user.service.OrderClient;
import com.store.user.service.UserService;
import com.store.user.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    OrderClient orderProxy;

    @Autowired
    Source source;

    @PostMapping("/users")
    public ResponseEntity insertUser(@RequestBody UserRequest userRequest) {
        userService.insertUser(userRequest);
        log.info("Creating new user");
        source.output().send(MessageBuilder.withPayload(AppConstants.CREATE_USER).build());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/list")
    public ResponseEntity getUserList() {
        log.info("Getting user list");
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PostMapping("/placeorder")
    public ResponseEntity placeOrder(@RequestBody OrderRequest orderRequest) {
        log.info("Got Place Order Request");
        source.output().send(MessageBuilder.withPayload(orderRequest).build());
        //orderService.placeOrder().send(MessageBuilder.withPayload(orderRequest).build());
        log.info("Order Placed ");
        return ResponseEntity.ok(orderRequest);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {

        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent()) {
            log.error("User ID not found ");
            throw new UserNotFoundException("User Not Found");
        }

        Long orders=orderProxy.getOrders(id);

        UserResponse userResponse=new UserResponse(user.get(),orders);

        log.info(String.format("Fetched User with id: %d ", id));
        return ResponseEntity.ok(userResponse);

    }

}
