package com.store.user.integration;

import com.google.gson.Gson;
import com.store.user.controller.UserController;
import com.store.user.jpa.entity.User;
import com.store.user.jpa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class IntegrationTest {

    @Autowired
    private UserController userController;
    @Autowired
    private UserRepository userRepository;
    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        gson=new Gson();

        ZoneId defaultZoneId =ZoneId.of("Europe/Paris");

        User user1 = new User(1L, "James", "Bond", Date.from(LocalDate.of(2018, 5, 14).atStartOfDay(defaultZoneId).toInstant()));
        User user2 = new User(2L, "Ethan", "Hunt", Date.from(LocalDate.of(2015, 3, 2).atStartOfDay(defaultZoneId).toInstant()));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        userRepository.saveAll(userList);


    }

    @Test
    public void testFlow() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/1")
        ).andExpect(status().isOk()).andReturn();

        User user = gson.fromJson(result.getResponse().getContentAsString(), User.class);
        assertEquals("James",user.getFirstName());
        assertEquals("Bond",user.getLastName());

    }



}
