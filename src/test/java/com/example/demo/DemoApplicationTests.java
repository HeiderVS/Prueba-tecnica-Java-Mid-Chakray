package com.example.demo;

import com.example.demo.Model.Address;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // Para convertir objetos a JSON y viceversa

    @MockBean
    private UserService userService;

    private List<User> userList;

    @BeforeEach
    public void setup() {
        // Crear usuarios de prueba
        userList = new ArrayList<>();
        User user1 = new User(1, "user1@mail.com", "user1", "password1", LocalDateTime.now(), new ArrayList<>(Arrays.asList(
                new Address(1L, "workaddress", "street No. 1", "UK"),
                new Address(2L, "homeaddress", "street No. 2", "AU")
        )));
        User user2 = new User(2, "user2@mail.com", "user2", "password2", LocalDateTime.now(), new ArrayList<>(Arrays.asList(
                new Address(1L, "workaddress", "street No. 1", "UK"),
                new Address(2L, "homeaddress", "street No. 2", "AU"))));
        userList.add(user1);
        userList.add(user2);
    }

}
