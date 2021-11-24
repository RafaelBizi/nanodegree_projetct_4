package com.example.demo;

import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author RafaelBizi
 * @created 19/11/2021 - 15:23
 * @project project_4_udacity
 */
public class UserTest {

    private BCryptPasswordEncoder bCryptPasswordEncoder = Mockito.mock(BCryptPasswordEncoder.class);
    private UserController userController;

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Before
    public void setUp() {
        userController = new UserController();

        TestsInjection.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        TestsInjection.injectObjects(userController, "cartRepository", cartRepository);
        TestsInjection.injectObjects(userController, "userRepository", userRepository);

        User user = new User();
        user.setId(1L);
        user.setUsername(TestUtils.USERNAME_TEST);
        user.setPassword(TestUtils.PASSWORD_TEST);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(TestUtils.USERNAME_TEST)).thenReturn(user);
    }

    @Test
    public void createUserSuccessTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(TestUtils.USERNAME_TEST, TestUtils.PASSWORD_TEST, TestUtils.CONFIRM_PASSWORD_TEST);

        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        Assert.assertEquals(200, userResponseEntity.getStatusCodeValue());
        Assert.assertEquals(TestUtils.USERNAME_TEST, userResponseEntity.getBody().getUsername());
    }

    @Test
    public void createUserBadRequestTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest(TestUtils.USERNAME_TEST, TestUtils.PASSWORD_TEST, "unknown");
        ResponseEntity<User> userResponseEntity = userController.createUser(createUserRequest);
        Assert.assertEquals(400, userResponseEntity.getStatusCodeValue());
        Assert.assertEquals("Password is different than the confirmed password!", userResponseEntity.getHeaders().getFirst("header1"));

        CreateUserRequest createUserRequest1 = new CreateUserRequest(TestUtils.USERNAME_TEST, TestUtils.PASSWORD_TOO_SHORT_TEST, TestUtils.PASSWORD_TOO_SHORT_TEST);
        ResponseEntity<User> userResponseEntity1 = userController.createUser(createUserRequest1);
        Assert.assertEquals(400, userResponseEntity1.getStatusCodeValue());
        Assert.assertEquals("Avoid empty or less than 8 chars password!", userResponseEntity1.getHeaders().getFirst("header2"));
    }

    @Test
    public void findUserByIdSuccessTest(){
        ResponseEntity<User> userResponseEntity = userController.findUserById(1L);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        assertEquals(TestUtils.USERNAME_TEST, userResponseEntity.getBody().getUsername());
    }

    @Test
    public void findUserByIdNotFoundTest(){
        ResponseEntity<User> userResponseEntity = userController.findUserById(10L);
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }

    @Test
    public void findUserByNameSuccessTest(){
        ResponseEntity<User> userResponseEntity = userController.findByUserName(TestUtils.USERNAME_TEST);
        assertEquals(200, userResponseEntity.getStatusCodeValue());
        assertEquals(TestUtils.USERNAME_TEST, userResponseEntity.getBody().getUsername());
    }

    @Test
    public void findUserByNameNotFoundTest(){
        ResponseEntity<User> userResponseEntity = userController.findByUserName("unknown");
        assertEquals(404, userResponseEntity.getStatusCodeValue());
    }

}
