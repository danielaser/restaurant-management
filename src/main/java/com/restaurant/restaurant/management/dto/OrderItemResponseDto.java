package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class OrderItemResponseDto {
    private Long idOrderItem;
    private Long idDish;
    private Integer quantity;
}

