package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.OrderItem;

public class OrderItemMapper {
    public static OrderItemResponseDto toDto(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setIdOrderItem(orderItem.getIdOrderItem());
        dto.setIdDish(orderItem.getDish().getIdDish());
        dto.setQuantity(orderItem.getQuantity());
        return dto;
    }

    public static OrderItem toEntity(OrderItemResponseDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setIdOrderItem(dto.getIdOrderItem());
        Dish dish = new Dish();
        dish.setIdDish(dto.getIdDish());
        orderItem.setDish(dish);
        orderItem.setQuantity(dto.getQuantity());
        return orderItem;
    }
}
