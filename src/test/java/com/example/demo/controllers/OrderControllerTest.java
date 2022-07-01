package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepo = mock(UserRepository.class);

    private OrderRepository orderRepo = mock(OrderRepository.class);


    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepo);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepo);

        User user = new User();
        user.setId(0L);
        user.setUsername("kim");
        user.setPassword("kim123");


        // Items that user has.
        Item item = new Item();
        item.setId(0L);
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        item.setDescription("This is black pencil");
        item.setName("pencil");
        List<Item> items = new ArrayList<>();
        items.add(item);


        Cart cart = new Cart();
        cart.setId(0L);
        cart.setItems(items);
        cart.setUser(user);
        cart.setTotal(price);


        user.setCart(cart);
        when(userRepo.findByUsername("test")).thenReturn(user);



    }

    @Test
    public void submit_order(){
        ResponseEntity<UserOrder> submit_order = orderController.submit("test");
        assertEquals(200, submit_order.getStatusCodeValue());
        UserOrder checkOrder = submit_order.getBody();
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        assert checkOrder != null;
        assertEquals(checkOrder.getTotal(), price);
    }

    @Test
    public void get_order_for_user(){
        ResponseEntity<List<UserOrder>> response_get_order = orderController.getOrdersForUser("test");
        assertEquals(200, response_get_order.getStatusCodeValue());
        List<UserOrder> checkList = response_get_order.getBody();
        assertNotNull(checkList);
    }

}
