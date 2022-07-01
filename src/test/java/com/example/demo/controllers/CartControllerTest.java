package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepo = mock(UserRepository.class);

    private CartRepository cartRepo = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    private ItemRepository itemRepo = mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepo);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepo);
//        TestUtils.injectObjects(cartController, "bCryptPasswordEncoder", encoder);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepo);
    }

    @Test
    public void add_to_cart(){

        User user = new User();
        Cart cart = new Cart();

        //user setting
        user.setId(0);
        user.setUsername("test");
        user.setPassword("test123");
        user.setCart(cart);
        when(userRepo.findByUsername("test")).thenReturn(user);

        //Item
        Item item = new Item();
        item.setId(1L);
        item.setName("Pencil");
        item.setDescription("this is black pencil");
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));


        //Cart setting
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        ArrayList<Item> cartItems = new ArrayList<>();
        cartItems.add(item);
        Cart checkCart = response.getBody();
        assert checkCart != null;
        assertEquals(checkCart.getItems(), cartItems);
        assertEquals(price, checkCart.getTotal());


    }

    @Test
    public void remove_cart(){
        //Scenario : Add 1 item, which has Id '1L' that has quantity 1, and remove this item from the cart. At the end, cart will be empty,
        // so that the number of items in cart will be 0.

        User user = new User();
        Cart cart = new Cart();

        //user setting
        user.setId(0);
        user.setUsername("test");
        user.setPassword("test123");
        user.setCart(cart);
        when(userRepo.findByUsername("test")).thenReturn(user);

        //Item
        Item item = new Item();
        item.setId(1L);
        item.setName("Pencil");
        item.setDescription("this is black pencil");
        double num = 5.88;
        BigDecimal price = new BigDecimal(num);
        item.setPrice(price);
        when(itemRepo.findById(1L)).thenReturn(Optional.of(item));


        //Cart setting
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername("test");
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertEquals(200, response.getStatusCodeValue());

        ModifyCartRequest modifyCartRequest1 = new ModifyCartRequest();
        modifyCartRequest1.setItemId(1L);
        modifyCartRequest1.setQuantity(1);
        modifyCartRequest1.setUsername("test");
        ResponseEntity<Cart> response1 = cartController.removeFromcart(modifyCartRequest1);
        assertEquals(200, response1.getStatusCodeValue());
        Cart emptyCart = response1.getBody();
        assert emptyCart != null;
        assertEquals(0, emptyCart.num_Items().intValue());
    }

}
