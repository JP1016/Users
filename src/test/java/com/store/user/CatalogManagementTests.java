package com.store.user;

import com.store.user.jpa.entity.User;
import com.store.user.jpa.repository.UserRepository;
import com.store.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CatalogManagementTests {

    @InjectMocks
    private UserService userService;

    @Spy
    private UserRepository userRepository;


    @BeforeEach
    public void setMockOutput() {

        ZoneId defaultZoneId = ZoneId.systemDefault();

        User user1=new User(1L,"James","Bond", Date.from(LocalDate.of(2018, 5, 14).atStartOfDay(defaultZoneId).toInstant()));
        User user2=new User(2L,"Ethan","Hunt", Date.from(LocalDate.of(2015, 3, 2).atStartOfDay(defaultZoneId).toInstant()));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        Mockito.lenient().when(userRepository.findAll()).thenReturn(userList);
        Mockito.lenient().when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

    }


    @DisplayName("User Details Testing")
    @Test
    public void checkUserDetails() {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        assertEquals("Ethan", userService.getUserById(2L).get().getFirstName());
        assertEquals("Hunt", userService.getUserById(2L).get().getLastName());
        assertEquals(Date.from(LocalDate.of(2015, 3, 2).atStartOfDay(defaultZoneId).toInstant()), userService.getUserById(2L).get().getDob());
        assertEquals(2,userRepository.findAll().size());

    }


}