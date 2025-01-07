package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class OrderResponseDto {
    private Long idOrder;
    private Double totalAmount;
    private List<OrderItemResponseDto> orderItems;
    private String clientName;
}


