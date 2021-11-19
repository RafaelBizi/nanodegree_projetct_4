package com.example.demo;

import com.example.demo.controllers.AuthenticationTestController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author RafaelBizi
 * @created 19/11/2021 - 17:00
 * @project project_4_udacity
 */
public class LoginTests {

    AuthenticationTestController authenticationTestController;

    @Before
    public void setUp() {
        authenticationTestController = new AuthenticationTestController();
    }

    @Test
    public void loginIsRequiredToAccessTheEndpointsTest() {
        Assert.assertEquals(200, authenticationTestController.test().getStatusCodeValue());
    }

}
