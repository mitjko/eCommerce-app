package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private static final String USERNAME = "mitjko";
    private static final String PASSWORD = "test123";

    private OrderController orderController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        orderController = new OrderController(userRepository, orderRepository);
    }

    @Test
    public void submit() {
        User user = createTestUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        ResponseEntity<UserOrder> response =  orderController.submit(USERNAME);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserOrder responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getItems());
        assertNotNull(responseBody.getTotal());
        assertNotNull(responseBody.getUser());
    }

    @Test
    public void submit_UserNotFound() {
        ResponseEntity<UserOrder> response =  orderController.submit(USERNAME);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    @Test
    public void getOrdersForUser() {
        User user = createTestUser();
        when(userRepository.findByUsername(USERNAME)).thenReturn(user);

        ResponseEntity<List<UserOrder>> response =  orderController.getOrdersForUser(USERNAME);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<UserOrder> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.isEmpty());
    }

    @Test
    public void getOrdersForUser_UserNotFound() {
        ResponseEntity<List<UserOrder>> response =  orderController.getOrdersForUser(USERNAME);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    private Cart createTestCart(User user){
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>(Arrays.asList(createTestItem())));
        cart.setTotal(BigDecimal.valueOf(0.0));
        return cart;
    }

    private User createTestUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setCart(createTestCart(user));
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