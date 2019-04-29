package com.store.user.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String firstName;
    private String lastName;
    private Date dob;

    public User(String firstName, String lastName, Date dob) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
    }
}
