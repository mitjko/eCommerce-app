package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private static final String NAME = "Mask";

    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        itemController = new ItemController(itemRepository);
    }

    @Test
    public void getItems() {
        List<Item> items = Arrays.asList(createTestItem());
        when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    public void getItemById() {
        Item item = createTestItem();
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(item, response.getBody());
    }

    @Test
    public void getItemById_NotFound() {
        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    @Test
    public void getItemsByName() {
        List<Item> items = Arrays.asList(createTestItem());
        when(itemRepository.findByName(NAME)).thenReturn(items);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(NAME);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(items, response.getBody());
    }

    @Test
    public void getItemsByName_NotFound() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName(NAME);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.hasBody());
    }

    private Item createTestItem() {
        Item item = new Item();
        item.setId(1L);
        item.setName(NAME);
        item.setDescription("Lorem ipsum");
        item.setPrice(BigDecimal.valueOf(1.0));
        return item;
    }
}