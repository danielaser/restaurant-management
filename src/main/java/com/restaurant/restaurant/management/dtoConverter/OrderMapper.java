package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.OrderResponseDto;
import com.restaurant.restaurant.management.models.Order;

import java.util.stream.Collectors;

public class OrderMapper {
    public static OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setIdOrder(order.getIdOrder());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderItems(order.getOrderItems().stream()
                .map(OrderItemMapper::toDto)
                .collect(Collectors.toList()));
        return dto;
    }

    public static Order toEntity(OrderResponseDto dto) {
        Order order = new Order();
        order.setIdOrder(dto.getIdOrder());
        order.setTotalAmount(dto.getTotalAmount());
        order.setOrderItems(dto.getOrderItems().stream()
                .map(OrderItemMapper::toEntity)
                .collect(Collectors.toList()));
        return order;
    }
}
