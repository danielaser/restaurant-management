package com.restaurant.restaurant.management.models;

import com.restaurant.restaurant.management.observer.DishObserver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DishTest {

    @Test
    @DisplayName("Crear plato con constructor completo")
    void createDishWithFullConstructor() {
        Menu menu = new Menu();
        menu.setIdMenu(1L);
        menu.setMenuName("Menu del lunes");

        Dish dish = new Dish(1L, "Pasta carbonara", 25.50, "Deliciosa pasta", menu);

        assertNotNull(dish);
        assertEquals(1L, dish.getIdDish());
        assertEquals("Pasta carbonara", dish.getDishName());
        assertEquals(25.50, dish.getPrice());
        assertEquals("Deliciosa pasta", dish.getDescription());
        assertEquals(menu, dish.getMenu());
    }

    @Test
    @DisplayName("Remover observador de la lista")
    void removeObserver() {
        Dish dish = new Dish();
        DishObserver observer = mock(DishObserver.class);

        dish.addObserver(observer);
        dish.removeObserver(observer);

        assertTrue(dish.getObservers().isEmpty());
    }

    @Test
    @DisplayName("Notificar a los observadores")
    void notifyObservers() {
        Dish dish = new Dish();
        DishObserver observer1 = mock(DishObserver.class);
        DishObserver observer2 = mock(DishObserver.class);

        dish.addObserver(observer1);
        dish.addObserver(observer2);

        dish.notifyObservers();

        verify(observer1).update(dish);
        verify(observer2).update(dish);
    }

}