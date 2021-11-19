package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.TestUtils.*;
import static org.mockito.Mockito.when;

/**
 * @author RafaelBizi
 * @created 18/11/2021 - 17:52
 * @project project_4_udacity
 */
public class ItemTests {

    private ItemController itemController;
    private ItemRepository itemRepository = Mockito.mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestsInjection.injectObjects(itemController, "itemRepository", itemRepository);

        Item item = TestUtils.newItem(1L);

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByName(item.getName())).thenReturn(Arrays.asList(item));
    }

    @Test
    public void getAllItemsTest() {
        ResponseEntity<List<Item>> itemsResponse = itemController.getItems();
        Assert.assertEquals(200, itemsResponse.getStatusCodeValue());
        Assert.assertEquals(1, itemsResponse.getBody().size());
    }

    @Test
    public void getItemsByIdSuccessTest() {
        ResponseEntity<Item> itemsResponse = itemController.getItemById(1L);
        Assert.assertEquals(200, itemsResponse.getStatusCodeValue());
        Assert.assertEquals(ITEM_NAME_TEST, itemsResponse.getBody().getName());
    }

    @Test
    public void getItemsByNameSuccessTest() {
        ResponseEntity<List<Item>> itemsResponse = itemController.getItemsByName(ITEM_NAME_TEST);
        Assert.assertEquals(200, itemsResponse.getStatusCodeValue());
        Assert.assertEquals(1, itemsResponse.getBody().size());
        Assert.assertEquals(Arrays.asList(new Item(1L,ITEM_NAME_TEST,ITEM_PRICE_TEST,ITEM_DESCRIPTION_TEST)),
                            Arrays.asList(itemsResponse.getBody().get(0)));
    }

    @Test
    public void getItemsByIdBadRequestTest() {
        ResponseEntity<Item> itemsResponse = itemController.getItemById(10L);
        Assert.assertEquals(404, itemsResponse.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameNotFoundTest() {
        ResponseEntity<List<Item>> itemsResponse = itemController.getItemsByName("unknown");
        Assert.assertEquals(404, itemsResponse.getStatusCodeValue());
    }

}
