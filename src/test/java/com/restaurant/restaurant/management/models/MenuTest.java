package com.restaurant.restaurant.management.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    @Test
    @DisplayName("Crear menú con constructor completo")
    void createMenuWithFullConstructor() {
        Menu menu = new Menu(1L, "Menu del domingo");

        assertNotNull(menu);
        assertEquals(1L, menu.getIdMenu());
        assertEquals("Menu del domingo", menu.getMenuName());
        assertNotNull(menu.getDishes());
        assertTrue(menu.getDishes().isEmpty());
    }

}