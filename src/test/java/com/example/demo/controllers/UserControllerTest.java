package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController  userController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void create_user_happy_path() throws  Exception{
        when(encoder.encode("testPassword")).thenReturn("thisIsHashed");


        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("test");
        r.setPassword("testPassword");
        r.setConfirmPassword("testPassword");

        ResponseEntity<User> response = userController.createUser(r);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("test", u.getUsername());
        assertEquals("thisIsHashed", u.getPassword());

    }

    @Test
    public void find_user_by_name(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test12");
        when(userRepo.findByUsername("test")).thenReturn(user);

        ResponseEntity<User> response = userController.findByUserName("test");
        assertEquals(200, response.getStatusCodeValue());

        User checkUser = response.getBody();
        assert checkUser != null;
        assertEquals(checkUser.getUsername(), "test");
        assertEquals(checkUser.getPassword(), "test12");

    }

    @Test
    public void find_user_by_id(){
        User user = new User();
        user.setId(2L);
        user.setUsername("test1");
        user.setPassword("test13");
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(2L);
        assertEquals(200, response.getStatusCodeValue());

        User checkUser = response.getBody();
        assert checkUser != null;
        assertEquals(checkUser.getUsername(), "test1");
        assertEquals(checkUser.getPassword(), "test13");
        assertEquals(checkUser.getId(), 2L);
    }

    @Test
    public void user_not_exist(){
        User user = new User();
        user.setId(2L);
        user.setUsername("test1");
        user.setPassword("test13");
        when(userRepo.findById(2L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.findById(3L);
        assertEquals(404, response.getStatusCodeValue());
    }

}
