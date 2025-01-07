package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.DishResponseDto;
import com.restaurant.restaurant.management.dto.OrderItemResponseDto;
import com.restaurant.restaurant.management.models.Dish;
import com.restaurant.restaurant.management.models.OrderItem;

public class OrderItemMapper {

    // Mapea la entidad OrderItem a DTO con detalles del Dish
    public static OrderItemResponseDto toDto(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        dto.setIdOrderItem(orderItem.getIdOrderItem());
        dto.setQuantity(orderItem.getQuantity());

        DishResponseDto dishDto = new DishResponseDto();
        dishDto.setIdDish(orderItem.getDish().getIdDish());
        dishDto.setDishName(orderItem.getDish().getDishName());
        dishDto.setPrice(orderItem.getDish().getPrice());
        dishDto.setDescription(orderItem.getDish().getDescription());

        dto.setDish(dishDto);
        return dto;
    }

    public static OrderItem toEntity(OrderItemResponseDto dto) {
        OrderItem orderItem = new OrderItem();
        orderItem.setIdOrderItem(dto.getIdOrderItem());
        orderItem.setQuantity(dto.getQuantity());

        Dish dish = new Dish();
        dish.setIdDish(dto.getDish().getIdDish()); 
        orderItem.setDish(dish);

        return orderItem;
    }
}
