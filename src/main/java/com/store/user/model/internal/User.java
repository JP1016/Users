package com.store.user.model.internal;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private Date dob;
}
