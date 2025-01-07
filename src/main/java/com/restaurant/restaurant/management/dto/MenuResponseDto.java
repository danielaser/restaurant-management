package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter

public class MenuResponseDto {
    private Long idMenu;
    private String menuName;
    private List<DishResponseDto> dishes;
}

