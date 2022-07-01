package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepo);

    }

    @Test
    public void get_items(){
        Item item = new Item();
        item.setName("pencil");
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        item.setId(1L);
        item.setDescription("this is black pencil");

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

//        Source: https://stackoverflow.com/questions/26027396/arrays-aslist-vs-collections-singletonlist
        when(itemRepo.findByName("pencil")).thenReturn(Collections.singletonList(item));
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));


        ResponseEntity<List<Item>> response = itemController.getItems();
        assertEquals(200, response.getStatusCodeValue());
        List<Item> checkList = response.getBody();
        assert checkList != null;
        //Since I put one item to the list, the size of list will be 1.
        assertEquals(1, checkList.size());
    }

    @Test
    public void get_item_by_id(){
        Item item = new Item();
        item.setName("pencil");
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        item.setId(1L);
        item.setDescription("this is black pencil");

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

//        Source: https://stackoverflow.com/questions/26027396/arrays-aslist-vs-collections-singletonlist
        when(itemRepo.findByName("pencil")).thenReturn(Collections.singletonList(item));
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertEquals(200, response.getStatusCodeValue());
//
        Item checkItem = response.getBody();
        assertEquals(checkItem.getPrice(), price);
        assertEquals(checkItem.getDescription(), "this is black pencil");
    }

    @Test
    public void get_item_by_name(){
        Item item = new Item();
        item.setName("pencil");
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        item.setId(1L);
        item.setDescription("this is black pencil");

        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));

//        Source: https://stackoverflow.com/questions/26027396/arrays-aslist-vs-collections-singletonlist
        when(itemRepo.findByName("pencil")).thenReturn(Collections.singletonList(item));
        when(itemRepo.findAll()).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("pencil");
        assertEquals(200, response.getStatusCodeValue());

        List<Item> checkList = response.getBody();
        assertEquals(checkList.size(), 1);

    }

}
