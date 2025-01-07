package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.models.OrderRestaurant;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponseDto toDto(OrderRestaurant order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setIdOrder(order.getIdOrder());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toList()));
        dto.setClientName(order.getClient().getClientName());
        return dto;
    }

    public static OrderRestaurant toEntity(OrderResponseDto dto) {
        OrderRestaurant order = new OrderRestaurant();
        order.setIdOrder(dto.getIdOrder());
        order.setTotalAmount(dto.getTotalAmount());
        order.setOrderItems(dto.getOrderItems() != null
                ? dto.getOrderItems().stream()
                .map(OrderItemMapper::toEntity)
                .collect(Collectors.toList())
                : new ArrayList<>());
        return order;
    }
}
