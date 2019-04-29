package com.store.user.service;

import com.store.user.exception.UserCreationFailedException;
import com.store.user.jpa.entity.User;
import com.store.user.jpa.repository.UserRepository;
import com.store.user.model.request.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertUser(UserRequest userRequest) throws UserCreationFailedException {
        try {
            User userEntity = new User(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getDob());
            userRepository.save(userEntity);
        } catch (Exception ex) {
            throw new UserCreationFailedException(ex.getMessage());

        }
    }


    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

}
