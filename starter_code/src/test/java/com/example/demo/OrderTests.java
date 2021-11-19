package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author RafaelBizi
 * @created 19/11/2021 - 15:07
 * @project project_4_udacity
 */
public class OrderTests {

    private OrderController orderController;
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();

        TestsInjection.injectObjects(orderController, "orderRepository", orderRepository);
        TestsInjection.injectObjects(orderController, "userRepository", userRepository);

        User user = TestUtils.newUser(1L);
        Item item = TestUtils.newItem(1L);

        Cart cart = new Cart();
        user.setCart(cart);
        cart.setUser(user);
        cart.addItem(item);

        UserOrder userOrder = UserOrder.createFromCart(cart);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(userOrder));
        when(userRepository.findByUsername(TestUtils.USERNAME_TEST)).thenReturn(user);
    }

    @Test
    public void getAllUserOrdersTest() {
        ResponseEntity<List<UserOrder>> listResponseEntity = orderController.getOrdersForUser(TestUtils.USERNAME_TEST);
        Assert.assertEquals(200, listResponseEntity.getStatusCodeValue());
        Assert.assertEquals(1, listResponseEntity.getBody().size());
    }

    @Test
    public void submitOrderSuccessTest() {
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit(TestUtils.USERNAME_TEST);
        Assert.assertEquals(200, orderResponseEntity.getStatusCodeValue());
        assertEquals(1, orderResponseEntity.getBody().getItems().size());
        assertEquals(TestUtils.USERNAME_TEST, orderResponseEntity.getBody().getUser().getUsername());
    }

    @Test
    public void submitOrderNotFoundTest() {
        ResponseEntity<UserOrder> orderResponseEntity = orderController.submit("unknown");
        Assert.assertEquals(404, orderResponseEntity.getStatusCodeValue());
    }

}
