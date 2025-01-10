package com.restaurant.restaurant.management.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderRestaurantTest {

    @Test
    @DisplayName("Crear OrderRestaurant con constructor completo")
    void createOrderRestaurantWithFullConstructor() {
        Client client = new Client();
        client.setIdClient(1L);
        client.setClientName("Dani");

        Dish dish = new Dish();
        dish.setIdDish(1L);
        dish.setDishName("Pizza");
        dish.setPrice(12.50);

        OrderItem orderItem = new OrderItem();
        orderItem.setIdOrderItem(1L);
        orderItem.setDish(dish);
        orderItem.setQuantity(2);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        OrderRestaurant orderRestaurant = new OrderRestaurant(1L, 25.00, orderItems, client);

        assertNotNull(orderRestaurant);
        assertEquals(1L, orderRestaurant.getIdOrder());
        assertEquals(25.00, orderRestaurant.getTotalAmount());
        assertNotNull(orderRestaurant.getOrderItems());
        assertEquals(1, orderRestaurant.getOrderItems().size());
        assertEquals(orderItem, orderRestaurant.getOrderItems().get(0));
        assertEquals(client, orderRestaurant.getClient());
    }

}