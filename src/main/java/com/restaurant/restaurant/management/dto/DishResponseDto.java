package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DishResponseDto {
    private Long idDish;
    private String dishName;
    private Double price;
    private String description;
    private Boolean isPopular;
}

