package com.store.user.controller;

import com.google.gson.Gson;
import com.store.user.exception.UserNotFoundException;
import com.store.user.jpa.entity.User;
import com.store.user.model.request.OrderRequest;
import com.store.user.model.request.UserRequest;
import com.store.user.model.response.UserResponse;
import com.store.user.service.OrderClient;
import com.store.user.service.UserService;
import com.store.user.utils.AppConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Spy
    OrderClient orderProxy;

    private MockMvc mockMvc;


    public List<User> getMockUserList()
    {
        ZoneId defaultZoneId =ZoneId.of("Europe/Paris");

        User user1 = new User(1L, "James", "Bond", Date.from(LocalDate.of(2018, 5, 14).atStartOfDay(defaultZoneId).toInstant()));
        User user2 = new User(2L, "Ethan", "Hunt", Date.from(LocalDate.of(2015, 3, 2).atStartOfDay(defaultZoneId).toInstant()));

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        return userList;
    }

    @BeforeEach
    public void setMockOutput() {
        Mockito.lenient().when(userService.findAllUsers()).thenReturn(getMockUserList());
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        Mockito.lenient().when(userService.insertUser(any(UserRequest.class))).thenReturn(1L);
        Mockito.lenient().when(userService.getUserById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(getMockUserList().get(0)));
        Mockito.lenient().when(orderProxy.getOrders(any(Long.class))).thenReturn(1L);

    }


    @Test
    void insertUser() throws Exception {
        String json="{" +
                "        \"firstName\": \"Matt\"," +
                "        \"lastName\": \"X\"," +
                "        \"dob\": \"2000-05-30\"" +
                "    }";
        Mockito.lenient().when(userService.insertUser(any(UserRequest.class))).thenReturn(1L);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(json)
        ).andExpect(status().isOk()).andReturn();

        assertEquals(200,result.getResponse().getStatus());


    }

    @Test
    void getUserList() throws Exception {


        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/list"))
                .andExpect(status().isOk()).andReturn();
        String content = result.getResponse().getContentAsString();
          Gson gson = new Gson();
        User[] userList = gson.fromJson(content, User[].class);

        assertEquals(2,userList.length);
        assertEquals("James",userList[0].getFirstName());
        assertEquals("Hunt",userList[1].getLastName());

    }

    @Test
    void placeOrder() throws Exception {
        String json="{\n" +
                "\"userId\" : 1,\n" +
                "\"productId\": 2,\n" +
                "\"orderDescription\" : \"Turbo Sale\",\n" +
                "\"orderProduct\" : \"Book\",\n" +
                "\"orderCode\":\"OD1249\"\n" +
                "}";

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/placeorder")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(json)
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        Gson gson=new Gson();
        OrderRequest orderRequest = gson.fromJson(content, OrderRequest.class);

        assertEquals("Book",orderRequest.getOrderProduct());

    }

    @Test
    void getUserOrders() throws Exception {
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/orders/1")
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        Gson gson=new Gson();
        UserResponse user = gson.fromJson(content, UserResponse.class);

        assertEquals("James",user.user.getFirstName());
        assertEquals((Long) 1L,user.getOrderCount());


    }

    @Test
    void getSuccessUserById() throws Exception{
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/1")
        ).andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        Gson gson=new Gson();
        User user = gson.fromJson(content, User.class);

        assertEquals("James",user.getFirstName());
    }

    @Test
    void getFailUserById() throws Exception {

        Mockito.lenient().when(userService.getUserById(any(Long.class))).thenThrow(new UserNotFoundException(AppConstants.USER_NOT_FOUND));


        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/api/users/1")
        ).andExpect(status().is4xxClientError()).andReturn();

        int content = result.getResponse().getStatus();

        assertEquals(404,content);

    }


}