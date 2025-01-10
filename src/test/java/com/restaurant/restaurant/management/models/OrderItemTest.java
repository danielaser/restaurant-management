package com.restaurant.restaurant.management.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {

    @Test
    @DisplayName("Crear OrderItem con constructor completo")
    void createOrderItemWithFullConstructor() {
        OrderRestaurant order = new OrderRestaurant();
        Dish dish = new Dish();
        dish.setIdDish(1L);
        dish.setDishName("Pizza");
        dish.setPrice(12.50);

        OrderItem orderItem = new OrderItem(1L, order, dish, 2);

        assertNotNull(orderItem);
        assertEquals(1L, orderItem.getIdOrderItem());
        assertEquals(order, orderItem.getOrder());
        assertEquals(dish, orderItem.getDish());
        assertEquals(2, orderItem.getQuantity());
    }

}