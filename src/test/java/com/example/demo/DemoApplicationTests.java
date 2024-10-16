package com.example.demo;

import com.example.demo.Controller.UserController;
import com.example.demo.Model.Address;
import com.example.demo.Model.DTO.UserDTO;
import com.example.demo.Model.Response;
import com.example.demo.Model.User;
import com.example.demo.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user1 = new User(1, "user1@mail.com", "user1", "password1", LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(
                        new Address(1L, "workaddress", "street No. 1", "UK"),
                        new Address(2L, "homeaddress", "street No. 2", "AU"))));

        user2 = new User(2, "user2@mail.com", "user2", "password2", LocalDateTime.now(),
                new ArrayList<>(Arrays.asList(
                        new Address(1L, "workaddress", "street No. 1", "UK"),
                        new Address(2L, "homeaddress", "street No. 2", "AU"))));

        userController.usuarios.add(user1);
        userController.usuarios.add(user2);
    }

    @Test
    void testSortedByEmail() {
        ResponseEntity<Response> responseEntity = userController.sortedBy("email");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getCode());
        assertEquals("All Users Sorted by email", responseEntity.getBody().getMessage());
        assertEquals("user1@mail.com", ((UserDTO) responseEntity.getBody().getUsers_info().get(0)).getEmail());
    }

    @Test
    void testSortedByInvalidField() {
        ResponseEntity<Response> responseEntity = userController.sortedBy("invalid_field");

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getCode());
        assertEquals("Invalid sortedBy value", responseEntity.getBody().getMessage());
    }

    @Test
    void testGetAddressesByUser() {
        ResponseEntity<Response> responseEntity = userController.getAdrressesByUser(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getCode());
        assertEquals("Addresses to user: 1", responseEntity.getBody().getMessage());
        assertEquals(2, responseEntity.getBody().getUsers_info().size());
    }

    @Test
    void testGetAddressesByUserNotFound() {
        ResponseEntity<Response> responseEntity = userController.getAdrressesByUser(100);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getCode());
        assertEquals("User not found", responseEntity.getBody().getMessage());
    }

    @Test
    void testUpdateAddressById() {
        Address updatedAddress = new Address(1L, "updatedWork", "updatedStreet", "UK");
        ResponseEntity<Response> responseEntity = userController.updateAddresseById(1, 1, updatedAddress);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getCode());
        assertEquals("From user: 1 Address: 1 was updated", responseEntity.getBody().getMessage());

        // Verificar que la dirección ha sido actualizada
        User user = userController.usuarios.stream().filter(u -> u.getId() == 1).findFirst().orElse(null);
        assertNotNull(user);
        Address address = user.getAddresses().stream().filter(a -> a.getId() == 1L).findFirst().orElse(null);
        assertNotNull(address);
        assertEquals("updatedWork", address.getName());
        assertEquals("updatedStreet", address.getStreet());
    }

    @Test
    void testSaveUser() throws NoSuchAlgorithmException {
        User newUser = new User(3, "user3@mail.com", "user3", "password3", LocalDateTime.now(), new ArrayList<>());
        when(userService.encodePassword(anyString())).thenReturn("encodedPassword");

        ResponseEntity<Response> responseEntity = userController.saveUser(newUser);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getCode());
        assertEquals("User created", responseEntity.getBody().getMessage());

        // Verificar que el usuario ha sido agregado
        assertEquals(6, userController.usuarios.size());
    }

    @Test
    void testDeleteUserById() {
        ResponseEntity<Response> responseEntity = userController.deleteUserById(1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(0, responseEntity.getBody().getCode());
        assertEquals("User deleted successfully", responseEntity.getBody().getMessage());

        // Verificar que el usuario ha sido eliminado
        assertEquals(4, userController.usuarios.size());
    }

    @Test
    void testDeleteUserByIdNotFound() {
        ResponseEntity<Response> responseEntity = userController.deleteUserById(100);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().getCode());
        assertEquals("User not found", responseEntity.getBody().getMessage());
    }
}
