package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.DishResponseDto;
import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.OrderItem;

public class OrderItemMapper {
    public static OrderItemResponseDto toDto(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setIdOrderItem(orderItem.getIdOrderItem());
        dto.setIdDish(orderItem.getDish().getIdDish()); // Solo el ID
        dto.setQuantity(orderItem.getQuantity());
        return dto;
    }

    public static OrderItem toEntity(OrderItemResponseDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setIdOrderItem(dto.getIdOrderItem());
        orderItem.setQuantity(dto.getQuantity());

        Dish dish = new Dish();
        dish.setIdDish(dto.getIdDish()); // Solo el ID
        orderItem.setDish(dish);

        return orderItem;
    }
}
