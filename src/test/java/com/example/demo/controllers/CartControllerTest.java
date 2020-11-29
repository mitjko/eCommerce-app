package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private static final String USERNAME = "mitjko";
    private static final String PASSWORD = "test123";

    private CartController cartController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        cartController = new CartController(userRepository, cartRepository, itemRepository);
    }

    @Test
    public void addToCart() {
        User user = createTestUser();
        Item item = createTestItem();
        Cart cart = user.getCart();
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(cartRepository.save(cart)).thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        verify(cartRepository, times(1)).save(cart);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void addToCart_UserNotFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void addToCart_ItemNotFound() {
        User user = createTestUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    @Test
    public void removeFromCart() {
        User user = createTestUser();
        Cart cart = user.getCart();
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(createTestItem()));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    public void removeFromCart_UserNotFound() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    @Test
    public void removeFromCart_ItemNotFound() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(createTestUser());

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest(USERNAME, 1L, 1);
        ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    private Cart createTestCart(){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(null);
        cart.setItems(new ArrayList<>(Arrays.asList(createTestItem())));
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    private User createTestUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCart(createTestCart());
        return user;
    }

    private Item createTestItem(){
        Item item = new Item();
        item.setId(1L);
        item.setName("Lorem ipsum");
        item.setDescription("Lorem ipsum");
        item.setPrice(BigDecimal.valueOf(1.0));
        return item;
    }
}