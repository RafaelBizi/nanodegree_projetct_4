package com.example.demo;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;

import java.math.BigDecimal;

/**
 * @author RafaelBizi
 * @created 18/11/2021 - 15:04
 * @project project_4_udacity
 */
public class TestUtils {

    public static final String USERNAME_TEST = "rafael";
    public static final String PASSWORD_TEST = "rafael12345";
    public static final String CONFIRM_PASSWORD_TEST = "rafael12345";
    public static final String PASSWORD_TOO_SHORT_TEST = "rafa12";
    public static final String ITEM_NAME_TEST = "ItemTesting";
    public static final BigDecimal ITEM_PRICE_TEST = new BigDecimal("50.7");
    public static final String ITEM_DESCRIPTION_TEST = "ItemDescription";

    public static User newUser(long id){
        User user = new User(id, USERNAME_TEST, PASSWORD_TEST);
        return user;
    }

    public static Item newItem(long id){
        Item item = new Item();
        item.setId(id);
        item.setName(ITEM_NAME_TEST);
        item.setPrice(ITEM_PRICE_TEST);
        item.setDescription(ITEM_DESCRIPTION_TEST);
        return item;
    }

}
