package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author RafaelBizi
 * @created 18/11/2021 - 15:18
 * @project project_4_udacity
 */
public class CartTests {

    private CartController cartController;

    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private CartRepository cartRepository = Mockito.mock(CartRepository.class);
    ModifyCartRequest cartRequest = new ModifyCartRequest(TestUtils.USERNAME_TEST, 1, 10);

    @Before
    public void setUp(){
        cartController = new CartController();

        User user = TestUtils.newUser(1L);
        Item item = TestUtils.newItem(1L);
        Cart cart = new Cart();
        cart.addItem(item);
        cart.setUser(user);
        user.setCart(cart);

        injectRepositories();

        Mockito.when(userRepository.findByUsername(TestUtils.USERNAME_TEST)).thenReturn(user);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addNewCartTest() {
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(cartRequest);
        Assert.assertEquals(200, cartResponseEntity.getStatusCodeValue());
        cartResponseEntity.getBody();
        Assert.assertEquals(10, cartResponseEntity.getBody().getItems().size());
    }

    @Test
    public void deleteCartTest() {
        ResponseEntity<Cart> cartResponse = cartController.removeFromcart(cartRequest);
        assertEquals(200, cartResponse.getStatusCodeValue());
        assertEquals(0, cartResponse.getBody().getItems().size());
    }

    @Test
    public void newCarBadRequestTest() {
        ModifyCartRequest badCartRequest1 = new ModifyCartRequest("anything", 1, 1);
        ResponseEntity<Cart> cartResponseEntity1 = cartController.addTocart(badCartRequest1);
        Assert.assertEquals(404, cartResponseEntity1.getStatusCodeValue());

        ModifyCartRequest badCartRequest2 = new ModifyCartRequest(TestUtils.USERNAME_TEST, 5, 1);
        ResponseEntity<Cart> cartResponseEntity2 = cartController.addTocart(badCartRequest2);
        Assert.assertEquals(404, cartResponseEntity2.getStatusCodeValue());
    }

    public void injectRepositories() {
        TestsInjection.injectObjects(cartController,"userRepository",userRepository);
        TestsInjection.injectObjects(cartController,"itemRepository",itemRepository);
        TestsInjection.injectObjects(cartController,"cartRepository",cartRepository);
    }

}
