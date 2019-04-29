package com.store.user.model.response;

import com.store.user.jpa.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    public User user;
    public Long orderCount;
}
